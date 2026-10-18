package com.ferrytech.android_poc.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;

import com.ferrytech.android_poc.R;
import com.ferrytech.android_poc.pageflip.ui.PageFlipView;
import com.ferrytech.android_poc.pageflip.ui.PageRender;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FlipBookActivity extends AppCompatActivity {

    private List<Bitmap> pdfPages = new ArrayList<>();
    private PageFlipView flipView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("FlipBookActivity.onCreate");
        setContentView(R.layout.activity_flip_book);

        flipView = findViewById(R.id.pageFlipView);

        try {

            // Load PDF
            File pdfFile = loadPdfFromAssets();

            // Convert PDF pages to Bitmaps
            pdfPages = renderPdfToBitmaps(pdfFile);
            Log.d("PDF_DEBUG",
                    "Total Pages = " + pdfPages.size());


            // Connect PDF pages with PageFlip library
// Connect PDF pages with PageFlip library
            flipView.setPageRender(new PageRender.RenderCallback() {

                @Override
                public Bitmap getBitmap(int pageNo) {
                    Log.d("PDF_DEBUG", "getBitmap pageNo = " + pageNo);
                    int index = pageNo - 1;

                    if (index >= 0 && index < pdfPages.size()) {
                        Log.d("PDF_DEBUG", "returning bitmap copy");

                        // THE FIX: Create a copy of the bitmap so the library doesn't destroy our original!
                        Bitmap originalBitmap = pdfPages.get(index);
                        return originalBitmap.copy(Bitmap.Config.ARGB_8888, false);
                    }

                    Log.d("PDF_DEBUG", "bitmap is NULL");
                    return null;
                }

                @Override
                public int getPageCount() {
                    return pdfPages.size();
                }

                @Override
                public boolean showPageNumber() {
                    return false;
                }
            });
            flipView.requestRender();

            flipView.setOnTouchListener((v, event) -> {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        Log.d("FLIP_TEST", "DOWN");
                        flipView.onFingerDown(
                                event.getX(),
                                event.getY());
                        break;

                    case MotionEvent.ACTION_MOVE:
                        Log.d("FLIP_TEST", "MOVE");
                        flipView.onFingerMove(
                                event.getX(),
                                event.getY());
                        break;

                    case MotionEvent.ACTION_UP:
                        Log.d("FLIP_TEST", "UP");
                        flipView.onFingerUp(
                                event.getX(),
                                event.getY());
                        break;
                }

                return true;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private File loadPdfFromAssets() throws Exception {

        File file = new File(getCacheDir(), "sample.pdf");
        if (file.exists()) {
            file.delete();
        }


            InputStream asset =
                    getAssets().open("sample.pdf");
            FileOutputStream output =
                    new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int size;

            while ((size = asset.read(buffer)) != -1) {
                output.write(buffer, 0, size);
            }

            asset.close();
            output.close();


        return file;
    }

    private List<Bitmap> renderPdfToBitmaps(File pdfFile) throws Exception {

        List<Bitmap> bitmaps =
                new ArrayList<>();

        ParcelFileDescriptor fileDescriptor =
                ParcelFileDescriptor.open(
                        pdfFile,
                        ParcelFileDescriptor.MODE_READ_ONLY);

        PdfRenderer renderer =
                new PdfRenderer(fileDescriptor);

        int pageCount =
                renderer.getPageCount();

        for (int i = 0; i < pageCount; i++) {

            PdfRenderer.Page page =
                    renderer.openPage(i);

            Bitmap bitmap =
                    Bitmap.createBitmap(
                            page.getWidth() * 2,
                            page.getHeight() * 2,
                            Bitmap.Config.ARGB_8888);

            page.render(
                    bitmap,
                    null,
                    null,
                    PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

            bitmaps.add(bitmap);

            page.close();
        }

        renderer.close();
        fileDescriptor.close();

        return bitmaps;
    }
}