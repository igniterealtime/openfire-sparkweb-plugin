package android.graphics;

public class Bitmap {
	
    public enum CompressFormat {
        JPEG    (0),
        PNG     (1),
        WEBP    (2);
        CompressFormat(int nativeInt) {
            this.nativeInt = nativeInt;
        }
        final int nativeInt;
    }	
	
}