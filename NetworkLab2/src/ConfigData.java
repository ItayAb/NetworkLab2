import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ConfigData {

	// constants for the config.ini
	private final String CONFIG_NAME = "config.ini";
	private final String MAX_DONWLOADERS = "maxDownloaders";
	private final String MAX_ANALYZERS = "maxAnalyzers";
	private final String IMAGE_EXTENSION = "defaultPage";
	private final String VIDEO_EXTENSION = "maxThreads";
	private final String DOCUMENT_EXTENSION = "documentExtension";
	
	// variables to hold the data
	private int m_MaxDownloads;
	private int m_MaxAnalyzers;
	private String m_ImageExtension;
	private int m_VideoExtension;
	private int[] m_DocumentExtension;

	// dynamically find the Config.ini path
	private String pathOfConfig;

	public ConfigData() {
		pathOfConfig = System.getProperty("user.dir");
	}

	public int getPort() {
		return m_MaxDownloads;
	}

	public String getRoot() {
		return m_MaxAnalyzers;
	}

	public String getDefaultPage() {
		return m_ImageExtension;
	}

	public int getMaxThreads() {
		return m_VideoExtension;
	}

	public void Load() throws Exception {

		File configIniFile = null;
		InputStream in = null;
		InputStreamReader read = null;
		BufferedReader dataReader = null;
		try {
			configIniFile = new File(pathOfConfig + File.separator + CONFIG_NAME);
			in = new FileInputStream(configIniFile);
			read = new InputStreamReader(in);
			dataReader = new BufferedReader(read);

			String lineInput;
			while (dataReader.ready()) {
				lineInput = dataReader.readLine();
				parserInputLine(lineInput);
			}
		} finally {
			if (in != null) {
				in.close();
			}
			if (read != null) {
				read.close();
			}
			if (dataReader != null) {
				dataReader.close();
			}
		}
	}

	private void parserInputLine(String lineOfInput) throws Exception{
		String value;
		String inputTrimmed = lineOfInput.trim();
		int indexOfEquals = inputTrimmed.indexOf('=');
		// check existence of '='
		if (indexOfEquals != -1) {
			// check existence of data past the '='
			if (inputTrimmed.length() > indexOfEquals) {
				// check port
				if (inputTrimmed.startsWith(MAX_DONWLOADERS)) {
					try {
						value = lineOfInput.substring(indexOfEquals + 1, lineOfInput.length()).trim();
						m_MaxDownloads = Integer.parseInt(value);						
					} catch (Exception e) {
						throw new ExceptionInInitializerError("Error with the port value!");
					}
				}
				// check root
				else if (inputTrimmed.startsWith(MAX_ANALYZERS)) {
					value = lineOfInput.substring(indexOfEquals + 1, lineOfInput.length()).trim();
					if (new File(value).exists() && new File(value).isDirectory()) {
						m_MaxAnalyzers = value;
					} else {
						throw new ExceptionInInitializerError("Error with the root path!");
					}
				}
				else if (inputTrimmed.startsWith(IMAGE_EXTENSION)) {
					value = lineOfInput.substring(indexOfEquals + 1, lineOfInput.length()).trim();
					if (new File(value).exists() && new File(value).isFile()) {
						m_ImageExtension = value;
					} else {
						throw new ExceptionInInitializerError("Error with the default page path!");
					}
				}

				// check max threads
				else if (inputTrimmed.startsWith(VIDEO_EXTENSION)) {
					try {
						value = lineOfInput.substring(indexOfEquals + 1, lineOfInput.length()).trim();
						m_VideoExtension = Integer.parseInt(value);
						if (m_VideoExtension < 1) {
							System.out.println("Error in maxThread value! please check the config.ini");
							throw new ExceptionInInitializerError("value must be a whole number and larger than 0");
						}
						
					} catch (Exception e) {						
						throw new ExceptionInInitializerError("Error in maxThread value! please check the config.ini");
					}
				}

			} else { // if there aren't any values past the '='
				throw new ExceptionInInitializerError("Data in the config.ini is malformed|corrupt");
			}
		} else {// in case there wasn't '='
			throw new ExceptionInInitializerError("Data in the config.ini is malformed|corrupt");
		}

	}
}
