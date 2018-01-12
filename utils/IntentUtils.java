public class IntentUtils {

    private static void openGalleryWithFile(Context context, File file) {
        StrictMode.VmPolicy old = StrictMode.getVmPolicy();
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "image/*");
        context.startActivity(intent);

        StrictMode.setVmPolicy(old);
    }
}
