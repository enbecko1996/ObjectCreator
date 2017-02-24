package com.enbecko.modcreator.geometry;

import com.enbecko.modcreator.Main_ModCreator;
import com.enbecko.modcreator.linalg.Matrix;
import com.enbecko.modcreator.linalg.vec3;

/**
 * Created by enbec on 07.01.2017.
 */
public class Bone {
    private CubicContentHolder boneContent = null;
    private vec3.DoubleVec rotPoint_global;
    private vec3.DoubleVec offset;
    private final Matrix.Matrix_NxN transform = Matrix.NxN_FACTORY.makeIdent(4);
    private final Matrix.Matrix_NxN inverseTransform = Matrix.NxN_FACTORY.makeIdent(4);

    public Bone() {

    }

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
            PrimitiveHolder base = new PrimitiveHolder(this, posInNext, (vec3.IntVec) posInOrder1.mulToThis(cpC), true);
            if (base.isFullInside(content))
                base.setMaxOrder(true);
            else {
                for (int k = 0; k < content.getCornerCount(); k++) {
                    vec3 cur = content.getCorner(k);
                    if (!base.isInside(cur)) {
                        tmpDouble.update(cur, false);
                        posInOrder1.update((vec3) tmpDouble.divToThis(cpC), true);
                    }
                }
            }
            System.out.println(posInNext+ " " + posInOrder1+" "+base.isFullInside(content));
        } else if ( !this.boneContent.isInside(content.getPositionInBoneCoords())) {

        } else {

        }
    }

    public static void main(String[] args) {
        Bone b;
        (b = new Bone()).addContent(new Visible_Cube(b, new vec3.IntVec(16, 4, 2, false), 2));
    }
}
