//Itay Abramowsky 304826688

import java.io.File;
import java.io.FileFilter;
import java.util.LinkedList;


public class Scouter implements Runnable {
	
	private File rootDirectory;
	private SynchronizedQueue<File> dir;
	
	public Scouter(SynchronizedQueue<File> directoryQueue, File root){
		rootDirectory = root;
		dir = directoryQueue;
		//setting thread as a producer
		dir.registerProducer();
	}
	
	@Override
	public void run() {
		//init linked list to perform the scouting iteratively
		LinkedList<File> directories = new LinkedList<File>();
		//add the root directory
		directories.add(rootDirectory);
		//if the linked list is not empty then scout
		while (!directories.isEmpty()) {
			//taking the first file
			File currentRoot = directories.poll();
			//enqueue the file
			dir.enqueue(currentRoot);
			
			//init sub array with all the sub direcories
			File[] sub = currentRoot.listFiles(new FileFilter() {
				
				@Override
				public boolean accept(File tmp) {
					return tmp.isDirectory();
				}
			});
			//add all the sub directories to the list
			if (sub!=null) {
				for (int i = 0; i < sub.length; i++) {
					directories.add(sub[i]);
				}				
			}
		}
		//thread is done, updating the producers
		dir.unregisterProducer();
		
	}

	
	
	

}
