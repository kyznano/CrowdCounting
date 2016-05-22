package hcmut.framework.lib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AppLibFile {
	public static Bitmap ccw(Bitmap b, int r)
	{
		Matrix m = new Matrix();
		m.postRotate(r);
		int w = b.getWidth();
		int h = b.getHeight();
		return Bitmap.createBitmap(b, 0, 0, w, h, m, true);
	}
	
	public static int calculateInSampleSize(int imgWidth, int imgHeight, int reqWidth, int reqHeight) {
		int inSampleSize = 1;
		if (reqHeight <= 0 || reqWidth <=0) {
			// do nothing here => inSampleSize = 1 => keep original size
		}else if (imgHeight > reqHeight || imgWidth > reqWidth) {
			final int halfHeight = imgHeight / 2;
			final int halfWidth = imgWidth / 2;
			// Calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}
		return inSampleSize;
	}
	
	public static Bitmap getBitmapFromPath(String path) {
		return getBitmapFromPath(path, -1, -1);
	}
	
	/**
	 * 
	 * @param path path to image
	 * @return int[] { width, height } or null if the path is not an image
	 */
	public static int[] getImageWidthHeight(String path) {
		if(!AppLibGeneral.isImageFile(path)) {
			return null;
		}		
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);	
		return new int[] { options.outWidth, options.outHeight };
	}
	
	@SuppressWarnings("deprecation")
	public static Bitmap getBitmapFromPath(String path, int reqWidth, int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);		
		options.inSampleSize = calculateInSampleSize(options.outWidth,options.outHeight,reqWidth,reqHeight); 
		options.inJustDecodeBounds = false;
		options.inPurgeable = true;
		Bitmap bitmapFile = BitmapFactory.decodeFile(path, options);
		
		if(bitmapFile!=null) {
	        try{
		        // rotate image if it is rotated
		        ExifInterface exif = new ExifInterface(path);
		        //exif.
				int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);			
				switch(orientation){
					case ExifInterface.ORIENTATION_ROTATE_90:
						bitmapFile = AppLibFile.ccw(bitmapFile, 90);
						break;
					case ExifInterface.ORIENTATION_ROTATE_270:
						bitmapFile = AppLibFile.ccw(bitmapFile, 270);
						break;
					case ExifInterface.ORIENTATION_ROTATE_180:
						bitmapFile = AppLibFile.ccw(bitmapFile, 180);
						break;
					default:
						break;
				}   	
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        return bitmapFile;
	}
	
	public static String writeBitmapToInternalStorage(Context context, Bitmap bm, String fileType) {
		if(bm==null) return "";
		String fileName = Long.toString(System.currentTimeMillis()) + fileType;
		FileOutputStream fos = null;
		try {
			fos = context.openFileOutput(fileName,Context.MODE_PRIVATE);
		} catch (FileNotFoundException e1) {
			//e1.printStackTrace();
		}
		if(fos!=null) {
			bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			File cacheDir = context.getFilesDir();
			String newpath = cacheDir.getPath() + File.separator + fileName;
			// set exifinterface for new image
			try {
				ExifInterface newExif = new ExifInterface(newpath);
				newExif.setAttribute(ExifInterface.TAG_ORIENTATION, Integer.toString(ExifInterface.ORIENTATION_NORMAL));
				newExif.saveAttributes();
			} catch (IOException e) {
				// e.printStackTrace();
			}	
			return newpath;
		} else return "";
	}
	
	public static String writeBitmapToInternalStorage(Context context, Bitmap bm, int quality_0_100) {
		if(bm==null) return "";
		String fileName = Long.toString(System.currentTimeMillis()) + ".jpg";
		FileOutputStream fos = null;
		try {
			fos = context.openFileOutput(fileName,Context.MODE_PRIVATE);
		} catch (FileNotFoundException fos_e) {
			//fos_e.printStackTrace();
		}
		if(fos!=null) {
			bm.compress(Bitmap.CompressFormat.JPEG, quality_0_100, fos);
			File cacheDir = context.getFilesDir();
			String newpath = cacheDir.getPath() + File.separator + fileName;
			// set exifinterface for new image
			try {
				ExifInterface newExif = new ExifInterface(newpath);
				newExif.setAttribute(ExifInterface.TAG_ORIENTATION, Integer.toString(ExifInterface.ORIENTATION_NORMAL));
				newExif.saveAttributes();
			} catch (IOException e) {
				// e.printStackTrace();
			}	
			return newpath;
		} else return "";
	}

	public static String writeBitmapToExternalStorage(Context context, Bitmap bm, int quality, String folderPath) {
		return writeBitmapToExternalStorage(context, bm, quality, folderPath, Long.toString(System.currentTimeMillis()));
	}

	/**
	 *
	 * @param context
	 * @param bm
	 * @param quality 0-100, 100 is maximum quality (no compression, largest file size)
	 * @param folderPath
	 * @param fileName
	 * @return
	 */
	public static String writeBitmapToExternalStorage(Context context, Bitmap bm, int quality, String folderPath, String fileName) {
		if(bm==null) return "";
		fileName += (fileName.endsWith(".jpg"))?"":".jpg";
		folderPath += (folderPath.endsWith(File.separator))?"":File.separator;
		try {
			String newPath = folderPath + fileName;
			FileOutputStream fos = new FileOutputStream(newPath, false);

			bm.compress(Bitmap.CompressFormat.JPEG, quality, fos);
			// set exifinterface for new image
			try {
				ExifInterface newExif = new ExifInterface(newPath);
				newExif.setAttribute(ExifInterface.TAG_ORIENTATION, Integer.toString(ExifInterface.ORIENTATION_NORMAL));
				newExif.saveAttributes();
			} catch (IOException e) {
				e.printStackTrace();
			}
			fos.close();
			return newPath;

		} catch (FileNotFoundException e) {
			return "";
		} catch (IOException e) {
			return "";
		}
	}

	/**
	 *
	 * @param path
	 * @return -1: cannot create dir; 0: dir existed; 1: created dir successfully
	 */
	public static int createDirIfNotExists(String path) {
		File file = new File(path);
		if (!file.exists()) {
			if (!file.mkdirs()) {
				return -1;
			} else {
				return 1;
			}
		}
		return 0;
	}

	public static String getExternalStoragePath(String folderName) {
		String path = "";
		File fileDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), folderName);
		if(!fileDir.exists()) {
			if(!fileDir.mkdir()) {
				path = "";
			} else {
				path = fileDir.getAbsolutePath();
			}
		} else {
			path = fileDir.getAbsolutePath();
		}
		return path;
	}
	
	public static void deleteFileInInternalStorage(Context context, String filePath) {		
		if (filePath.length()>0) {
			String fp = filePath;
			String contextFilesPath = context.getFilesDir().getPath();
			if(fp.contains(contextFilesPath)) { // only delete files are in internal storage
//				String fileName = fp.replace(contextFilesPath + File.separator, "");
				String[] fileNameAndExtension = extractFileNameAndExtension(fp);
				String fileName = fileNameAndExtension[0] + "." + fileNameAndExtension[1];
				context.deleteFile(fileName);	
			}			
		}
	}
	
	public static String[] extractFileNameAndExtension(String filePath) {
		String[] nameAndExtension = null;
		if(filePath.length()>0) {
			String[] f = filePath.split(File.separator);
			String nameExt = f[f.length-1];
			int lastIndexOfDot = nameExt.lastIndexOf(".");
			if(lastIndexOfDot>=0) {
				String name = nameExt.substring(0, lastIndexOfDot);
				String extension = nameExt.substring(lastIndexOfDot);
				nameAndExtension = new String[2];
				nameAndExtension[0] = name;
				nameAndExtension[1] = extension;
			}
		}
		return nameAndExtension;
	}
	
}
