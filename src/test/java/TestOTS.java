//
// Java code to access Root file of TH1F objects
// Using the root4j package

// Names of the objects are wfe-b where
//   e is the event number
//   b is the board number (1-4)
//
// Each object contains 16000 values,
// these are 16 waveforms of 1000 values each
//
import java.io.IOException;

import org.dianahep.root4j.RootClassNotFound;
import org.dianahep.root4j.RootFileReader;
import org.dianahep.root4j.interfaces.TH1F;

public class TestOTS
{
	public static void main(String[] argv) throws IOException
	{
		String file = "/tmp/Artdaq1-simpler.root";
		if (argv.length > 0) {
			file = argv[0];
		}
		RootFileReader rfr = new RootFileReader(file);
		int nKeys = rfr.nKeys();
		int nEvents = nKeys/4;
		System.out.println("File has " + nEvents + " events");


		for (int i = 1; i < nEvents; i++) {   // loop over events
			for (int j = 1; j < 5; j++) {     // loop over boards
				String name = "wf" + i + "-" + j;
				try {
					TH1F histogram = (TH1F) rfr.get(name);
					float[] values = histogram.getArray();
					int nMeas = 16;				  // 16 waveforms/data block
					int nPts = values.length/nMeas;
					double[] mean = new double[nMeas];
					for (int k = 0; k < nMeas; k++) { // loop over waveforms in a measurement
						mean[k] = 0;
						for (int l = k*nPts; l < (k+1)*nPts; l++) {
							mean[k] = mean[k] + values[l];
						}
						mean[k] = mean[k]/nPts;
					}
				} catch (Exception e) {
					System.out.println("Exception reading record " + name + ": " + e);
				}
			}
		}

	}    
}