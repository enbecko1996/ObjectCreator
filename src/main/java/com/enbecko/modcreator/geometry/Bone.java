package com.enbecko.modcreator.geometry;

import com.enbecko.modcreator.linalg.Matrix;
import com.enbecko.modcreator.linalg.vec3;
import com.enbecko.modcreator.linalg.vec_n;

import static com.enbecko.modcreator.linalg.vec_n.vecPrec.INT;

/**
 * Created by enbec on 07.01.2017.
 */
public class Bone {
    @Deprecated
    private CubicContentHolderGeometry boneContent = null;
    private vec3.DoubleVec rotPoint_global;
    private vec3.DoubleVec offset;
    private final Matrix.Matrix_NxN transform = Matrix.NxN_FACTORY.makeIdent(4);
    private final Matrix.Matrix_NxN inverseTransform = Matrix.NxN_FACTORY.makeIdent(4);
    private final Octant I, II, III, IV, V, VI, VII, VIII;
    private final Octant[] octants = new Octant[8];

    public Bone() {
        vec3.IntVec center = new vec3.IntVec();
        this.I = new Octant(this, center, 1, 1, 1, Octant.OCTANTS.I);
        this.II = new Octant(this, (vec3.IntVec) center.addAndMakeNew(INT, false, -1, 0, 0), 1, 1, 1, Octant.OCTANTS.II);
        this.III = new Octant(this, (vec3.IntVec) center.addAndMakeNew(INT, false, -1, -1, 0), 1, 1, 1, Octant.OCTANTS.III);
        this.IV = new Octant(this, (vec3.IntVec) center.addAndMakeNew(INT, false, 0, -1, 0), 1, 1, 1, Octant.OCTANTS.IV);
        this.V = new Octant(this, (vec3.IntVec) center.addAndMakeNew(INT, false, 0, 0, -1), 1, 1, 1, Octant.OCTANTS.V);
        this.VI = new Octant(this, (vec3.IntVec) center.addAndMakeNew(INT, false, -1, 0, -1), 1, 1, 1, Octant.OCTANTS.VI);
        this.VII = new Octant(this, (vec3.IntVec) center.addAndMakeNew(INT, false, -1, -1, -1), 1, 1, 1, Octant.OCTANTS.VII);
        this.VIII = new Octant(this, (vec3.IntVec) center.addAndMakeNew(INT, false, 0, -1, -1), 1, 1, 1, Octant.OCTANTS.VIII);
        this.octants[0] = this.I;
        this.octants[1] = this.II;
        this.octants[2] = this.III;
        this.octants[3] = this.IV;
        this.octants[4] = this.V;
        this.octants[5] = this.VI;
        this.octants[6] = this.VII;
        this.octants[7] = this.VIII;
    }

    /**
    @Deprecated
    public void addContent(Content content, Content ... adjacent) {
        if (this.boneContent == null) {
            int cpC = Main_ModCreator.contentCubesPerCube;
            vec3 pos = content.getPositionInBoneCoords();
            vec3.DoubleVec tmpDouble = new vec3.DoubleVec(pos, false);
            vec3.IntVec posInOrder1 = new vec3.IntVec((vec3) tmpDouble.divToThis(cpC), true);
            int diff;
            vec3.ByteVec posInNext = new vec3.ByteVec((diff = (posInOrder1.getX() % cpC)) >= 0 ? diff : cpC + diff,
                    (diff = (posInOrder1.getY() % cpC)) >= 0 ? diff : cpC + diff,
                    (diff = (posInOrder1.getZ() % cpC)) >= 0 ? diff : cpC + diff ,
                    false);
            FirstOrderHolder base = new FirstOrderHolder(this, posInNext, (vec3.IntVec) posInOrder1.mulToThis(cpC), false);
            base.addContent(content);
            if (base.isFullInside(content)) {
                base.setMaxOrder(true);
                this.boneContent = base;
            } else {
                int order = 2;
                int len = (int) Math.pow(cpC, order);
                posInOrder1.update(tmpDouble.update(pos, false).divToThis(len), true);
                posInNext.update((diff = (posInOrder1.getX() % len)) >= 0 ? diff : cpC + diff,
                        (diff = (posInOrder1.getY() % len)) >= 0 ? diff : cpC + diff,
                        (diff = (posInOrder1.getZ() % len)) >= 0 ? diff : cpC + diff,
                        false);
                HigherOrderHolder order2 = new HigherOrderHolder(this, posInNext, (vec3.IntVec) posInOrder1.mulToThis(len), (byte) order, true);
                base.setMaxOrder(false);
                base.setParent(order2);
                order2.addContent(base);
                for (int k = 0; k < content.getCornerCount(); k++) {
                    vec3 cur = content.getCorner(k);
                    if (!base.isInside(cur)) {
                        tmpDouble.update(cur, false);
                        posInOrder1.update(tmpDouble.update(pos, false).divToThis(cpC), true);
                        posInNext.update((diff = (posInOrder1.getX() % cpC)) >= 0 ? diff : cpC + diff,
                                (diff = (posInOrder1.getY() % cpC)) >= 0 ? diff : cpC + diff,
                                (diff = (posInOrder1.getZ() % cpC)) >= 0 ? diff : cpC + diff,
                                false);
                        FirstOrderHolder newHold = new FirstOrderHolder(this, posInNext, (vec3.IntVec) posInOrder1.mulToThis(cpC), false);
                        if (order2.isInside(cur)) {
                            order2.addContent(newHold);
                        } else {
                            order = 2;
                            len = (int) Math.pow(cpC, order);
                            posInOrder1.update(tmpDouble.update(cur, false).divToThis(len), true);
                            posInNext.update((diff = (posInOrder1.getX() % len)) >= 0 ? diff : cpC + diff,
                                    (diff = (posInOrder1.getY() % len)) >= 0 ? diff : cpC + diff,
                                    (diff = (posInOrder1.getZ() % len)) >= 0 ? diff : cpC + diff,
                                    false);
                            HigherOrderHolder order2_2 = new HigherOrderHolder(this, posInNext, (vec3.IntVec) posInOrder1.mulToThis(len), (byte) order, true);
                            newHold.setMaxOrder(false);
                            newHold.setParent(order2_2);
                            order2_2.addContent(newHold);

                        }
                    }
                }
            }
            System.out.println(posInNext+ " " + posInOrder1+" "+base.isFullInside(content));
        } else if ( !this.boneContent.isInside(content.getPositionInBoneCoords())) {

        } else {

        }
    }
    */

    public void addContent(Content content, Content ... adjacent) {

    }

    public void octantEmpty(Octant octant) {
        for (Octant octant1 : this.octants) {
            if (octant == octant1)
                octant1.setActive(false);
        }
    }

    public static void main(String[] args) {
        Bone b;
        (b = new Bone()).addContent(new Visible_Cube(b, new vec3.IntVec(16, 4, 2, false), 2));
    }
}
