package com.enbecko.modcreator.linalg;

import com.enbecko.modcreator.Log;
import com.enbecko.modcreator.Log.LogEnums;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Niclas on 22.03.2016.
 */
public class MathHelper {

    public static double getVecLength(double...vec)
    {
        double tmp = 0;
        for(int k = 0; k < vec.length; k++)
        {
            tmp+= Math.pow(vec[k], 2);
        }
        return Math.sqrt(tmp);
    }

    public static float getAngleBetween(vec3.DoubleVec first, vec3.DoubleVec sec)
    {
        return (float) Math.toDegrees(Math.acos((first.getX() * sec.getX() + first.getY() * sec.getY() + first.getZ() * sec.getZ()) / (getVecLength(first) * getVecLength(sec))));
    }

    public static float getAngleBetweenZ(vec3.DoubleVec first, vec3.DoubleVec sec)
    {
        return (float) Math.toDegrees(Math.acos((first.getX() * sec.getX() + first.getY() * sec.getY()) / (getVecLength(first.getX(), first.getY()) * getVecLength(sec.getX(), sec.getY()))));
    }

    public static float getAngleBetweenX(vec3.DoubleVec first, vec3.DoubleVec sec)
    {
        return (float) Math.toDegrees(Math.acos((first.getZ() * sec.getZ() + first.getY() * sec.getY()) / (getVecLength(first.getZ(), first.getY()) * getVecLength(sec.getZ(), sec.getY()))));
    }

    public static boolean sameSign(float a, float b) {
        return a*b >= 0.0f;
    }

    public static float getVecLength(float x, float y)
    {
        double tmp = 0;
        tmp = Math.pow(x, 2)+Math.pow(y, 2);
        return (float)Math.sqrt(tmp);
    }

    public static float getVecLength(vec3.DoubleVec vec)
    {
        double tmp = 0;
        tmp = Math.pow(vec.getX(), 2)+Math.pow(vec.getY(), 2)+Math.pow(vec.getZ(), 2);
        return (float)Math.sqrt(tmp);
    }

    public static float getVecLength(vec3 vec)
    {
        double tmp = 0;
        tmp = Math.pow(vec.getXD(), 2)+Math.pow(vec.getYD(), 2)+Math.pow(vec.getZD(), 2);
        return (float)Math.sqrt(tmp);
    }

    public static double getSmallest(double ... choose) {
        double tt = Double.POSITIVE_INFINITY;
        for (double tmp : choose) {
            if (tmp < tt)
                tt = tmp;
        }
        return tt;
    }

    public static double getBiggest(double ... choose) {
        double tt = Double.NEGATIVE_INFINITY;
        for (double tmp : choose) {
            if (tmp > tt)
                tt = tmp;
        }
        return tt;
    }

    /**
     * @return an array containing the line
     * @throws ArrayStoreException if the runtime type of the specified array
     *         is not a supertype of the runtime type of every element in
     *         this list
     * @throws NullPointerException if the specified array is null
     */
    @Nullable
    private static vec3[] lineBetween(@Nonnull vec3.vecPrec prec, vec3 p1, vec3 p2, final double oneUnit) {
        List<vec3> tmpVecsX = new ArrayList<vec3>(), tmpVecsY = new ArrayList<vec3>(), tmpVecsZ = new ArrayList<vec3>();
        vec3.DoubleVec delta = (vec3.DoubleVec) new vec3.DoubleVec(p2, false).subFromThis(p1);
        double mY = delta.getY() / delta.getX();
        float up = 0;
        int upSteps = 0;
        Log.d(LogEnums.MATH, p1+" "+p2+" "+delta+" "+mY);
        for (double x = p1.getXD(); x <= p2.getXD(); x += oneUnit) {
            up += mY * oneUnit;
            if(up >= oneUnit) {
                upSteps++;
                up -= oneUnit;
            }
            addToList(prec, tmpVecsZ, x, upSteps * oneUnit, p1.getZD());
        }
        double mZ = delta.getY() / delta.getZ();
        up = 0;
        upSteps = 0;
        Log.d(LogEnums.MATH, p1+" "+p2+" "+delta+" "+mZ);
        for (double z = p1.getZD(); z <= p2.getZD(); z += oneUnit) {
            up += mZ * oneUnit;
            if(up >= oneUnit) {
                upSteps++;
                up -= oneUnit;
            }
            addToList(prec, tmpVecsX, p1.getXD(), upSteps * oneUnit, z);
        }
        double mX = delta.getZ() / delta.getX();
        up = 0;
        upSteps = 0;
        Log.d(LogEnums.MATH, p1+" "+p2+" "+delta+" "+mX);
        for (double x = p1.getXD(); x <= p2.getXD(); x += oneUnit) {
            up += mX * oneUnit;
            if(up >= oneUnit) {
                upSteps++;
                up -= oneUnit;
            }
            addToList(prec, tmpVecsX, x, p1.getYD(), upSteps * oneUnit);
        }
        Log.d(LogEnums.MATH, tmpVecsX+" "+tmpVecsY+" "+tmpVecsZ);
        vec3[] out = null;
        switch (prec) {
            case BYTE:
                out = new vec3.ByteVec[tmpVecsX.size()];
                break;
            case SHORT:
                out = new vec3.ShortVec[tmpVecsX.size()];
                break;
            case INT:
                out = new vec3.IntVec[tmpVecsX.size()];
                break;
            case LONG:
                out = new vec3.LongVec[tmpVecsX.size()];
                break;
            case FLOAT:
                out = new vec3.FloatVec[tmpVecsX.size()];
                break;
            case DOUBLE:
                out = new vec3.DoubleVec[tmpVecsX.size()];
                break;
        }
        return tmpVecsX.toArray(out);
    }

    private static void addToList(vec3.vecPrec prec, List<vec3> list, double x, double y, double z) {
        switch (prec) {
            case BYTE:
                list.add(new vec3.ByteVec((byte) x, (byte) y, (byte) z, false));
                break;
            case SHORT:
                list.add(new vec3.ShortVec((short) x, (short) y, (short) z, false));
                break;
            case INT:
                list.add(new vec3.IntVec((int) x, (int) y, (int) z, false));
                break;
            case LONG:
                list.add(new vec3.LongVec((long) x, (long) y, (long) z, false));
                break;
            case FLOAT:
                list.add(new vec3.FloatVec((float) x, (float) y, (float) z, false));
                break;
            case DOUBLE:
                list.add(new vec3.DoubleVec(x, y, z, false));
                break;
        }
    }

    /**
     * @return an array containing the line
     * @throws ArrayStoreException if the runtime type of the specified array
     *         is not a supertype of the runtime type of every element in
     *         this list
     * @throws NullPointerException if the specified array is null
     */
    @Nullable
    public static vec3.IntVec[] lineBetweenI(vec3.IntVec first, vec3.IntVec sec, int unit) {
        return (vec3.IntVec[]) lineBetween(vec_n.vecPrec.INT, first, sec, unit);
    }

    /**
     * @return an array containing the line
     * @throws ArrayStoreException if the runtime type of the specified array
     *         is not a supertype of the runtime type of every element in
     *         this list
     * @throws NullPointerException if the specified array is null
     */
    @Nullable
    public static vec3.ByteVec[] lineBetweenB(vec3.ByteVec first, vec3.ByteVec sec, byte unit) {
        return (vec3.ByteVec[]) lineBetween(vec_n.vecPrec.BYTE, first, sec, unit);
    }

    /**
     * @return an array containing the line
     * @throws ArrayStoreException if the runtime type of the specified array
     *         is not a supertype of the runtime type of every element in
     *         this list
     * @throws NullPointerException if the specified array is null
     */
    @Nullable
    public static vec3.ShortVec[] lineBetweenS(vec3.ShortVec first, vec3.ShortVec sec, short unit) {
        return (vec3.ShortVec[]) lineBetween(vec_n.vecPrec.SHORT, first, sec, unit);
    }

    /**
     * @return an array containing the line
     * @throws ArrayStoreException if the runtime type of the specified array
     *         is not a supertype of the runtime type of every element in
     *         this list
     * @throws NullPointerException if the specified array is null
     */
    @Nullable
    public static vec3.LongVec[] lineBetweenL(vec3.LongVec first, vec3.LongVec sec, long unit) {
        return (vec3.LongVec[]) lineBetween(vec_n.vecPrec.LONG, first, sec, unit);
    }

    /**
     * @return an array containing the line
     * @throws ArrayStoreException if the runtime type of the specified array
     *         is not a supertype of the runtime type of every element in
     *         this list
     * @throws NullPointerException if the specified array is null
     */
    @Nullable
    public static vec3.FloatVec[] lineBetweenF(vec3.FloatVec first, vec3.FloatVec sec, float unit) {
        return (vec3.FloatVec[]) lineBetween(vec_n.vecPrec.FLOAT, first, sec, unit);
    }

    /**
     * @return an array containing the line
     * @throws ArrayStoreException if the runtime type of the specified array
     *         is not a supertype of the runtime type of every element in
     *         this list
     * @throws NullPointerException if the specified array is null
     */
    @Nullable
    public static vec3.DoubleVec[] lineBetweenD(vec3.DoubleVec first, vec3.DoubleVec sec, double unit) {
        return (vec3.DoubleVec[]) lineBetween(vec_n.vecPrec.DOUBLE, first, sec, unit);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Number> void addGenericVec(Class type, List<vec3Gen<T>> vec3List, double x, double y, double z) {
        if(type == Double.class)
            vec3List.add(new vec3Gen<T>((T) Double.valueOf(x), (T) Double.valueOf(y), (T) Double.valueOf(z)));
        else if(type == Float.class)
            vec3List.add(new vec3Gen<T>((T) Float.valueOf((float) x), (T) Float.valueOf((float) y), (T) Float.valueOf((float) z)));
        else if(type == Integer.class)
            vec3List.add(new vec3Gen<T>((T) Integer.valueOf((int) x), (T) Integer.valueOf((int) y), (T) Integer.valueOf((int) z)));
        else if(type == Long.class)
            vec3List.add(new vec3Gen<T>((T) Long.valueOf((long) x), (T) Long.valueOf((long) y), (T) Long.valueOf((long) z)));
    }
}
