//Itay Abramowsky 304826688

import java.io.File;
import java.io.FileFilter;

public class Searcher implements Runnable {

	private SynchronizedQueue<File> dir;
	private SynchronizedQueue<File> result;
	private String extension;

	public Searcher(String extension, SynchronizedQueue<File> directoryQueue, SynchronizedQueue<java.io.File> resultsQueue) {
	
		this.extension = extension;
		dir = directoryQueue;
		this.result = resultsQueue;
		//since the thread will enqueue then we register it as a producer
		this.result.registerProducer();
	}

	@Override
	public void run() {
		
		while (true) {
			//dequeue a file from the synchronized queue
			File f = dir.dequeue();
			
			//if the file dequeue is null then thread finishes 
			if (f == null) {
				//update the registers
				result.unregisterProducer();
				return;
				
			}
			//init an array with all the files that match the extentsion
			File[] subFile = f.listFiles(new FileFilter() {
				@Override
				public boolean accept(File file) {
					return file.getName().endsWith("." + extension);
				}
			});
			//if no file match the extension then continue
			if (subFile == null) {
				continue;
			}
			//enqueue all the file to the result queue to be copied
			for (int i = 0; i < subFile.length; i++) {
				result.enqueue(subFile[i]);
			}
		}


	}

}
