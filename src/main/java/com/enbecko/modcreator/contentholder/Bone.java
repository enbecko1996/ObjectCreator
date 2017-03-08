package com.enbecko.modcreator.contentholder;

import com.enbecko.modcreator.Visible.Gridded_CUBE;
import com.enbecko.modcreator.events.BlockSetModes.BlockSetMode;
import com.enbecko.modcreator.linalg.Matrix;
import com.enbecko.modcreator.linalg.RayTrace3D;
import com.enbecko.modcreator.linalg.vec3;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

import static com.enbecko.modcreator.linalg.vec_n.vecPrec.INT;

/**
 * Created by enbec on 07.01.2017.
 */
public class Bone {
    @Deprecated
    private CubicContentHolderGeometry boneContent = null;
    private vec3 center_global;
    private vec3.DoubleVec offset;
    private final Matrix.Matrix_NxN transform = Matrix.NxN_FACTORY.makeIdent(4);
    private final Matrix.Matrix_NxN inverseTransform = Matrix.NxN_FACTORY.makeIdent(4);
    private final Octant I, II, III, IV, V, VI, VII, VIII;
    private final Octant[] octants = new Octant[8];
    protected final List<Octant> rayTraceResult = new ArrayList<Octant>();
    protected double[] distance = new double[8];

    @SideOnly(Side.CLIENT)
    public Bone(vec3 center_global) {
        this.center_global = center_global;
        System.out.println(this.center_global);
        this.transform.translate(this.center_global.getVecD());
        ((Matrix.Matrix_NxN)this.inverseTransform.update(transform)).invert();
        System.out.println(transform + " " + this.inverseTransform);
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
    public void addNewChild(Content content, Content ... adjacent) {
        if (this.boneContent == null) {
            int cpC = Main_BlockHeroes.contentCubesPerCube;
            vec3 pos = content.getPositionInBoneCoords();
            vec3.DoubleVec tmpDouble = new vec3.DoubleVec(pos, false);
            vec3.IntVec posInOrder1 = new vec3.IntVec((vec3) tmpDouble.divToThis(cpC), true);
            int diff;
            vec3.ByteVec posInNext = new vec3.ByteVec((diff = (posInOrder1.getX() % cpC)) >= 0 ? diff : cpC + diff,
                    (diff = (posInOrder1.getY() % cpC)) >= 0 ? diff : cpC + diff,
                    (diff = (posInOrder1.getZ() % cpC)) >= 0 ? diff : cpC + diff ,
                    false);
            FirstOrderHolder base = new FirstOrderHolder(this, posInNext, (vec3.IntVec) posInOrder1.mulToThis(cpC), false);
            base.addNewChild(content);
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
                order2.addNewChild(base);
                for (int k = 0; k < content.getBoundingCornerCount(); k++) {
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
                            order2.addNewChild(newHold);
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
                            order2_2.addNewChild(newHold);

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

    public RayTraceResult getRayTraceResult(RayTrace3D rayTrace_GLOBAL, BlockSetMode editMode) {
        RayTrace3D rayTrace_BONE = new RayTrace3D(rayTrace_GLOBAL);
        rayTrace_BONE.transform(this.inverseTransform);
        this.rayTraceResult.clear();
        for (int l = 0; l < distance.length; l++) {
            if (distance[l] != 0)
                distance[l] = 0;
            else
                break;
        }
        vec3 pos;
        for (Octant octant : this.octants) {
            if (octant.isActive()) {
                if (octant.isInside(rayTrace_BONE.getOnPoint())) {
                    this.rayTraceResult.add(0, octant);
                    double tmp = distance[0];
                    for (int l = 1; l < distance.length; l++) {
                        if (l > 0 && distance[l - 1] != 0) {
                            double tt = distance[l];
                            distance[l] = tmp;
                            tmp = tt;
                        } else
                            break;
                    }
                }
                if ((pos = octant.checkIfCrosses(rayTrace_BONE)) != null) {
                    double d = pos.subFromThis(rayTrace_BONE.getOnPoint()).length();
                    int k = 0;
                    for (; k < distance.length; k++) {
                        if (distance[k] == 0 || distance[k] > d) {
                            if (distance[k] != 0) {
                                double tmp = distance[k];
                                for (int l = k + 1; l < distance.length; l++) {
                                    if (l > 0 && distance[l - 1] != 0) {
                                        double tt = distance[l];
                                        distance[l] = tmp;
                                        tmp = tt;
                                    } else
                                        break;
                                }
                            }
                            distance[k] = d;
                            break;
                        }
                    }
                    this.rayTraceResult.add(k, octant);
                }
            }
        }
        for (Octant octant : this.rayTraceResult) {
            Content result;
            if ((result = octant.getRayTraceResult(rayTrace_BONE)) != null)
                return new RayTraceResult(this, rayTrace_BONE, result, result.getCrossedFaceVecAndAngle(rayTrace_BONE, editMode));
        }
        return null;
    }

    @SideOnly(Side.CLIENT)
    public void render() {
        for (Octant octant : this.octants)
            octant.render();
    }

    public void addContent(Content content, Content ... adjacent) {
        if (!this.I.isActive())
            this.I.setActive(true);
        if (!this.II.isActive())
            this.II.setActive(true);
        Gridded_CUBE griddedCUBE1 = new Gridded_CUBE(this, new vec3.IntVec(0, 0, 0), 1).createBoundingGeometry();
        this.I.addContent(new vec3.IntVec(0, 0, 0), griddedCUBE1);
        Gridded_CUBE griddedCUBE2 = new Gridded_CUBE(this, new vec3.IntVec(-1, 0, 0), 1).createBoundingGeometry();
        this.II.addContent(new vec3.IntVec(-1, 0, 0), griddedCUBE2);
    }

    public void octantEmpty(Octant octant) {
        for (Octant octant1 : this.octants) {
            if (octant == octant1)
                octant1.setActive(false);
        }
    }

    private static final vec3.DoubleVec eye = new vec3.DoubleVec(39, 100.5, 123.5), look = new vec3.DoubleVec(-1, 0, 0);
    private static final RayTrace3D theRayTrace = new RayTrace3D(eye, look, 100, true);

    public static void main(String[] args) {
        Bone b = new Bone(new vec3.IntVec(30, 100, 123));
        b.addContent(null);
        theRayTrace.update(eye, look);
        long time = System.currentTimeMillis();
        System.out.println(System.currentTimeMillis() - time);
    }
}
