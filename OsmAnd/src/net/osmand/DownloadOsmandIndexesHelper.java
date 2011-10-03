package net.osmand;

import java.io.IOException;
import java.net.URL;

import net.osmand.activities.OsmandApplication;
import net.osmand.data.index.IndexConstants;

import org.apache.commons.logging.Log;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;

public class DownloadOsmandIndexesHelper {
	private final static Log log = LogUtil.getLog(DownloadOsmandIndexesHelper.class);
	
	public static IndexFileList downloadIndexesListFromInternet(){
		try {
			log.debug("Start loading list of index files"); //$NON-NLS-1$
			IndexFileList result = new IndexFileList();
				try {
					URL url = new URL("http://download.osmand.net/get_indexes?" + Version.getVersionAsURLParam()); //$NON-NLS-1$
					XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
					parser.setInput(url.openStream(), "UTF-8"); //$NON-NLS-1$
					int next;
					while((next = parser.next()) != XmlPullParser.END_DOCUMENT) {
						if(next == XmlPullParser.START_TAG && (parser.getName().equals("region") ||
								parser.getName().equals("multiregion"))) { //$NON-NLS-1$
							String name = parser.getAttributeValue(null, "name"); //$NON-NLS-1$
							String size = parser.getAttributeValue(null, "size"); //$NON-NLS-1$
							String date = parser.getAttributeValue(null, "date"); //$NON-NLS-1$
							String description = parser.getAttributeValue(null, "description"); //$NON-NLS-1$
							String parts = parser.getAttributeValue(null, "parts"); //$NON-NLS-1$
							result.add(name, new IndexItem(name, description, date, size, parts));
						} else if (next == XmlPullParser.START_TAG && ("osmand_regions".equals(parser.getName()))) {
							String mapversion = parser.getAttributeValue(null, "mapversion");
							result.setMapVersion(mapversion);
						}
					}
				} catch (IOException e) {
					log.error("Error while loading indexes from repository", e); //$NON-NLS-1$
					return null;
				} catch (XmlPullParserException e) {
					log.error("Error while loading indexes from repository", e); //$NON-NLS-1$
					return null;
				}
			
			if (result.isAcceptable()) {
				return result;
			} else {
				return null;
			}
		} catch (RuntimeException e) {
			log.error("Error while loading indexes from repository", e); //$NON-NLS-1$
			return null;
		}
	}

	public static class IndexItem {
		private String description;
		private String date;
		private String parts;
		private String fileName;
		private String size;
		
		public IndexItem(String fileName, String description, String date, String size, String parts) {
			this.fileName = fileName;
			this.description = description;
			this.date = date;
			this.size = size;
			this.parts = parts;
		}
		
		public String getVisibleDescription(Context ctx){
			String s = ""; //$NON-NLS-1$
			if (fileName.endsWith(IndexConstants.POI_INDEX_EXT) || fileName.endsWith(IndexConstants.POI_INDEX_EXT_ZIP)) {
				s = ctx.getString(R.string.poi);
			} else if (fileName.endsWith(IndexConstants.ADDRESS_INDEX_EXT) || fileName.endsWith(IndexConstants.ADDRESS_INDEX_EXT_ZIP)) {
				s = ctx.getString(R.string.address);
			} else if (fileName.endsWith(IndexConstants.TRANSPORT_INDEX_EXT)
					|| fileName.endsWith(IndexConstants.TRANSPORT_INDEX_EXT_ZIP)) {
				s = ctx.getString(R.string.transport);
			} else if (fileName.endsWith(IndexConstants.BINARY_MAP_INDEX_EXT)
					|| fileName.endsWith(IndexConstants.BINARY_MAP_INDEX_EXT_ZIP)) {
				boolean f = true;
				String lowerCase = description.toLowerCase();
				if (lowerCase.contains("map")) { //$NON-NLS-1$
					if (!f) {
						s += ", "; //$NON-NLS-1$
					} else {
						f = false;
					}
					s += ctx.getString(R.string.map_index);
				}
				if (lowerCase.contains("transport")) { //$NON-NLS-1$
					if (!f) {
						s += ", "; //$NON-NLS-1$
					} else {
						f = false;
					}
					s += ctx.getString(R.string.transport);
				}
				if (lowerCase.contains("address")) { //$NON-NLS-1$
					if (!f) {
						s += ", "; //$NON-NLS-1$
					} else {
						f = false;
					}
					s += ctx.getString(R.string.address);
				}
			} else if (fileName.endsWith(IndexConstants.VOICE_INDEX_EXT_ZIP)) {
				s = ctx.getString(R.string.voice);
			}
			return s;
		}
		
		public String getVisibleName(){
			int l = fileName.lastIndexOf('_');
			String name = fileName.substring(0, l < 0 ? fileName.length() : l).replace('_', ' ');
			if (fileName.endsWith(".zip")) { //$NON-NLS-1$
				name += " (zip)"; //$NON-NLS-1$
			}
			return name;
		}
		
		public boolean isAccepted(){
			if (fileName.endsWith(IndexConstants.POI_INDEX_EXT) || fileName.endsWith(IndexConstants.POI_INDEX_EXT_ZIP)) {
				return true;
			} else if (fileName.endsWith(IndexConstants.ADDRESS_INDEX_EXT) || fileName.endsWith(IndexConstants.ADDRESS_INDEX_EXT_ZIP)) {
				return true;
			} else if (fileName.endsWith(IndexConstants.TRANSPORT_INDEX_EXT)
					|| fileName.endsWith(IndexConstants.TRANSPORT_INDEX_EXT_ZIP)) {
				return true;
			} else if (fileName.endsWith(IndexConstants.BINARY_MAP_INDEX_EXT)
					|| fileName.endsWith(IndexConstants.BINARY_MAP_INDEX_EXT_ZIP)) {
				return true;
			} else if (fileName.endsWith(IndexConstants.VOICE_INDEX_EXT_ZIP)) {
				return true;
			}
			return false;
		}
		
		public String getFileName() {
			return fileName;
		}
		public String getDescription() {
			return description;
		}
		public String getDate() {
			return date;
		}
		
		public String getSize() {
			return size;
		}
		
		public String getParts() {
			return parts;
		}
		
	}
	
}
