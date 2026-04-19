import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class Index2DTest {
    @Test
    public void testGetXY() {
        Index2D point = new Index2D(5, 3);
        assertEquals(5, point.getX());
        assertEquals(3, point.getY());
    }
    @Test
    public void testDistance2D() {
        Index2D start = new Index2D(0, 4);
        Index2D end = new Index2D(3, 0);
        double result = start.distance2D(end);
        assertEquals(5, result);
    }
    @Test
    public void testToString() {
        Index2D pos = new Index2D(4, 5);
        String ans = pos.toString();
        assertEquals("4,5", ans);
    }
    @Test
    public void testEquals() {
        Index2D p0 = new Index2D(4, 5);
        Index2D p1 = new Index2D(p0);
        assertEquals(p1, p0);
    }
}
