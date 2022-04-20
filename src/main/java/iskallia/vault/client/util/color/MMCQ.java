package iskallia.vault.client.util.color;

import javax.annotation.Nullable;
import java.util.*;

public class MMCQ {
    private static final int SIGBITS = 5;
    private static final int RSHIFT = 3;
    private static final int MULT = 8;
    private static final int HISTOSIZE = 32768;
    private static final int VBOX_LENGTH = 32;
    private static final double FRACT_BY_POPULATION = 0.75;
    private static final int MAX_ITERATIONS = 1000;
    private static final Comparator<VBox> COMPARATOR_COUNT;
    private static final Comparator<VBox> COMPARATOR_PRODUCT;

    static int getColorIndex(final int r, final int g, final int b) {
        return (r << 10) + (g << 5) + b;
    }

    private static int[] getHisto(final int[][] pixels) {
        final int[] histo = new int[32768];
        for (final int[] pixel : pixels) {
            final int rval = pixel[0] >> 3;
            final int gval = pixel[1] >> 3;
            final int bval = pixel[2] >> 3;
            final int index = getColorIndex(rval, gval, bval);
            final int[] array = histo;
            final int n = index;
            ++array[n];
        }
        return histo;
    }

    private static VBox vboxFromPixels(final int[][] pixels, final int[] histo) {
        int rmin = 1000000;
        int rmax = 0;
        int gmin = 1000000;
        int gmax = 0;
        int bmin = 1000000;
        int bmax = 0;
        for (final int[] pixel : pixels) {
            final int rval = pixel[0] >> 3;
            final int gval = pixel[1] >> 3;
            final int bval = pixel[2] >> 3;
            if (rval < rmin) {
                rmin = rval;
            } else if (rval > rmax) {
                rmax = rval;
            }
            if (gval < gmin) {
                gmin = gval;
            } else if (gval > gmax) {
                gmax = gval;
            }
            if (bval < bmin) {
                bmin = bval;
            } else if (bval > bmax) {
                bmax = bval;
            }
        }
        return new VBox(rmin, rmax, gmin, gmax, bmin, bmax, histo);
    }

    private static VBox[] medianCutApply(final int[] histo, final VBox vbox) {
        if (vbox.count(false) == 0) {
            return null;
        }
        if (vbox.count(false) == 1) {
            return new VBox[]{vbox.clone(), null};
        }
        final int rw = vbox.r2 - vbox.r1 + 1;
        final int gw = vbox.g2 - vbox.g1 + 1;
        final int bw = vbox.b2 - vbox.b1 + 1;
        final int maxw = Math.max(Math.max(rw, gw), bw);
        int total = 0;
        final int[] partialsum = new int[32];
        Arrays.fill(partialsum, -1);
        final int[] lookaheadsum = new int[32];
        Arrays.fill(lookaheadsum, -1);
        if (maxw == rw) {
            for (int i = vbox.r1; i <= vbox.r2; ++i) {
                int sum = 0;
                for (int j = vbox.g1; j <= vbox.g2; ++j) {
                    for (int k = vbox.b1; k <= vbox.b2; ++k) {
                        final int index = getColorIndex(i, j, k);
                        sum += histo[index];
                    }
                }
                total += sum;
                partialsum[i] = total;
            }
        } else if (maxw == gw) {
            for (int i = vbox.g1; i <= vbox.g2; ++i) {
                int sum = 0;
                for (int j = vbox.r1; j <= vbox.r2; ++j) {
                    for (int k = vbox.b1; k <= vbox.b2; ++k) {
                        final int index = getColorIndex(j, i, k);
                        sum += histo[index];
                    }
                }
                total += sum;
                partialsum[i] = total;
            }
        } else {
            for (int i = vbox.b1; i <= vbox.b2; ++i) {
                int sum = 0;
                for (int j = vbox.r1; j <= vbox.r2; ++j) {
                    for (int k = vbox.g1; k <= vbox.g2; ++k) {
                        final int index = getColorIndex(j, k, i);
                        sum += histo[index];
                    }
                }
                total += sum;
                partialsum[i] = total;
            }
        }
        for (int i = 0; i < 32; ++i) {
            if (partialsum[i] != -1) {
                lookaheadsum[i] = total - partialsum[i];
            }
        }
        return (maxw == rw) ? doCut('r', vbox, partialsum, lookaheadsum, total) : ((maxw == gw) ? doCut('g', vbox, partialsum, lookaheadsum, total) : doCut('b', vbox, partialsum, lookaheadsum, total));
    }

    private static VBox[] doCut(final char color, final VBox vbox, final int[] partialsum, final int[] lookaheadsum, final int total) {
        int vbox_dim1;
        int vbox_dim2;
        if (color == 'r') {
            vbox_dim1 = vbox.r1;
            vbox_dim2 = vbox.r2;
        } else if (color == 'g') {
            vbox_dim1 = vbox.g1;
            vbox_dim2 = vbox.g2;
        } else {
            vbox_dim1 = vbox.b1;
            vbox_dim2 = vbox.b2;
        }
        VBox vbox2 = null;
        VBox vbox3 = null;
        for (int i = vbox_dim1; i <= vbox_dim2; ++i) {
            if (partialsum[i] > total / 2) {
                vbox2 = vbox.clone();
                vbox3 = vbox.clone();
                final int left = i - vbox_dim1;
                final int right = vbox_dim2 - i;
                int d2;
                if (left <= right) {
                    d2 = Math.min(vbox_dim2 - 1, ~(~(i + right / 2)));
                } else {
                    d2 = Math.max(vbox_dim1, ~(~(int) (i - 1 - left / 2.0)));
                }
                while (d2 < 0 || partialsum[d2] <= 0) {
                    ++d2;
                }
                for (int count2 = lookaheadsum[d2]; count2 == 0 && d2 > 0 && partialsum[d2 - 1] > 0; count2 = lookaheadsum[--d2]) {
                }
                if (color == 'r') {
                    vbox2.r2 = d2;
                    vbox3.r1 = d2 + 1;
                } else if (color == 'g') {
                    vbox2.g2 = d2;
                    vbox3.g1 = d2 + 1;
                } else {
                    vbox2.b2 = d2;
                    vbox3.b1 = d2 + 1;
                }
                return new VBox[]{vbox2, vbox3};
            }
        }
        throw new RuntimeException("VBox can't be cut");
    }

    @Nullable
    public static CMap quantize(final int[][] pixels, final int maxcolors) {
        if (pixels.length == 0 || maxcolors < 1 || maxcolors > 256) {
            return null;
        }
        final int[] histo = getHisto(pixels);
        final VBox vbox = vboxFromPixels(pixels, histo);
        final ArrayList<VBox> pq = new ArrayList<VBox>();
        pq.add(vbox);
        final int target = (int) Math.ceil(0.75 * maxcolors);
        iter(pq, MMCQ.COMPARATOR_COUNT, target, histo);
        Collections.sort(pq, MMCQ.COMPARATOR_PRODUCT);
        iter(pq, MMCQ.COMPARATOR_PRODUCT, maxcolors - pq.size(), histo);
        Collections.reverse(pq);
        final CMap cmap = new CMap();
        for (final VBox vb : pq) {
            cmap.push(vb);
        }
        return cmap;
    }

    private static void iter(final List<VBox> lh, final Comparator<VBox> comparator, final int target, final int[] histo) {
        int ncolors = 1;
        int niters = 0;
        while (niters < 1000) {
            final VBox vbox = lh.get(lh.size() - 1);
            if (vbox.count(false) == 0) {
                Collections.sort(lh, comparator);
                ++niters;
            } else {
                lh.remove(lh.size() - 1);
                final VBox[] vboxes = medianCutApply(histo, vbox);
                final VBox vbox2 = vboxes[0];
                final VBox vbox3 = vboxes[1];
                if (vbox2 == null) {
                    throw new RuntimeException("vbox1 not defined; shouldn't happen!");
                }
                lh.add(vbox2);
                if (vbox3 != null) {
                    lh.add(vbox3);
                    ++ncolors;
                }
                Collections.sort(lh, comparator);
                if (ncolors >= target) {
                    return;
                }
                if (niters++ > 1000) {
                    return;
                }
                continue;
            }
        }
    }

    static {
        COMPARATOR_COUNT = new Comparator<VBox>() {
            @Override
            public int compare(final VBox a, final VBox b) {
                return a.count(false) - b.count(false);
            }
        };
        COMPARATOR_PRODUCT = new Comparator<VBox>() {
            @Override
            public int compare(final VBox a, final VBox b) {
                final int aCount = a.count(false);
                final int bCount = b.count(false);
                final int aVolume = a.volume(false);
                final int bVolume = b.volume(false);
                if (aCount == bCount) {
                    return aVolume - bVolume;
                }
                return aCount * aVolume - bCount * bVolume;
            }
        };
    }

    public static class VBox {
        int r1;
        int r2;
        int g1;
        int g2;
        int b1;
        int b2;
        private final int[] histo;
        private int[] _avg;
        private Integer _volume;
        private Integer _count;

        public VBox(final int r1, final int r2, final int g1, final int g2, final int b1, final int b2, final int[] histo) {
            this.r1 = r1;
            this.r2 = r2;
            this.g1 = g1;
            this.g2 = g2;
            this.b1 = b1;
            this.b2 = b2;
            this.histo = histo;
        }

        @Override
        public String toString() {
            return "r1: " + this.r1 + " / r2: " + this.r2 + " / g1: " + this.g1 + " / g2: " + this.g2 + " / b1: " + this.b1 + " / b2: " + this.b2;
        }

        public int volume(final boolean force) {
            if (this._volume == null || force) {
                this._volume = (this.r2 - this.r1 + 1) * (this.g2 - this.g1 + 1) * (this.b2 - this.b1 + 1);
            }
            return this._volume;
        }

        public int count(final boolean force) {
            if (this._count == null || force) {
                int npix = 0;
                for (int i = this.r1; i <= this.r2; ++i) {
                    for (int j = this.g1; j <= this.g2; ++j) {
                        for (int k = this.b1; k <= this.b2; ++k) {
                            final int index = MMCQ.getColorIndex(i, j, k);
                            npix += this.histo[index];
                        }
                    }
                }
                this._count = npix;
            }
            return this._count;
        }

        public VBox clone() {
            return new VBox(this.r1, this.r2, this.g1, this.g2, this.b1, this.b2, this.histo);
        }

        public int[] avg(final boolean force) {
            if (this._avg == null || force) {
                int ntot = 0;
                int rsum = 0;
                int gsum = 0;
                int bsum = 0;
                for (int i = this.r1; i <= this.r2; ++i) {
                    for (int j = this.g1; j <= this.g2; ++j) {
                        for (int k = this.b1; k <= this.b2; ++k) {
                            final int histoindex = MMCQ.getColorIndex(i, j, k);
                            final int hval = this.histo[histoindex];
                            ntot += hval;
                            rsum += (int) (hval * (i + 0.5) * 8.0);
                            gsum += (int) (hval * (j + 0.5) * 8.0);
                            bsum += (int) (hval * (k + 0.5) * 8.0);
                        }
                    }
                }
                if (ntot > 0) {
                    this._avg = new int[]{~(~(rsum / ntot)), ~(~(gsum / ntot)), ~(~(bsum / ntot))};
                } else {
                    this._avg = new int[]{~(~(8 * (this.r1 + this.r2 + 1) / 2)), ~(~(8 * (this.g1 + this.g2 + 1) / 2)), ~(~(8 * (this.b1 + this.b2 + 1) / 2))};
                }
            }
            return this._avg;
        }

        public boolean contains(final int[] pixel) {
            final int rval = pixel[0] >> 3;
            final int gval = pixel[1] >> 3;
            final int bval = pixel[2] >> 3;
            return rval >= this.r1 && rval <= this.r2 && gval >= this.g1 && gval <= this.g2 && bval >= this.b1 && bval <= this.b2;
        }
    }

    public static class CMap {
        public final ArrayList<VBox> vboxes;

        public CMap() {
            this.vboxes = new ArrayList<VBox>();
        }

        public void push(final VBox box) {
            this.vboxes.add(box);
        }

        public int[][] palette() {
            final int numVBoxes = this.vboxes.size();
            final int[][] palette = new int[numVBoxes][];
            for (int i = 0; i < numVBoxes; ++i) {
                palette[i] = this.vboxes.get(i).avg(false);
            }
            return palette;
        }

        public int size() {
            return this.vboxes.size();
        }

        @Nullable
        public int[] map(final int[] color) {
            for (int numVBoxes = this.vboxes.size(), i = 0; i < numVBoxes; ++i) {
                final VBox vbox = this.vboxes.get(i);
                if (vbox.contains(color)) {
                    return vbox.avg(false);
                }
            }
            return this.nearest(color);
        }

        @Nullable
        public int[] nearest(final int[] color) {
            double d1 = Double.MAX_VALUE;
            int[] pColor = null;
            for (int numVBoxes = this.vboxes.size(), i = 0; i < numVBoxes; ++i) {
                final int[] vbColor = this.vboxes.get(i).avg(false);
                final double d2 = ColorUtil.fastPerceptualColorDistanceSquared(color, vbColor);
                if (d2 < d1) {
                    d1 = d2;
                    pColor = vbColor;
                }
            }
            return pColor;
        }
    }
}
