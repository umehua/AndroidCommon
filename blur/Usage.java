
public class Usage {

    public static void main(String[] args) {
        Usage usage = new Usage();
        usage.getBlurWallpaper();     
    }

    private Bitmap getBlurWallpaper() {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        Drawable wallpaperDrawable = wallpaperManager.getDrawable();
        Bitmap wallpaperBitmap = ((BitmapDrawable) wallpaperDrawable).getBitmap();

        wallpaperBitmap = Bitmap.createScaledBitmap(wallpaperBitmap, wallpaperBitmap.getWidth()/10,
                wallpaperBitmap.getHeight()/10, false);

        StackBlurManager blurManager = new StackBlurManager(wallpaperBitmap);
        blurManager.process(20);
        wallpaperBitmap = blurManager.returnBlurredImage();

        return wallpaperBitmap;
    }
}
