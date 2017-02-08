//
// Root code to access Root file of TH1F objects

// Names of the objects are wfe-b where
//   e is the event number
//   b is the board number (1-4)
//
// Each object contains 16000 values,
// these are 16 waveforms of 1000 values each
//

void testots() {

TFile *f = new TFile("/tmp/Artdaq1-simpler.root");

int nKeys = f->GetNkeys();
int nEvents = nKeys/4;

std::cout << "Number of keys: " << nKeys << " Number of events: " << nEvents << std::endl;

char name[10];
for (int i = 1; i <= nEvents; i++) { // Loop over events
  for (int j =1; j < 5; j++) {       // Loop over boards
    sprintf(name, "wf%d-%d", i, j);

    TH1F* h = (TH1F*)f->Get(name);

    if (h == 0) {
        cout << name << " pointer is null!" << endl;
    } else {

        const int nMeas = 16;            // #measurements/data block
       int nPts = h->GetNbinsX()/nMeas;// Points per waveform

// Compute mean for each of the 16 waveforms
        double mean[nMeas];
        for (int k = 0; k < nMeas; k++) {
            mean[k] = 0;
            for (int l = k*nPts; l < k*nPts+nPts; l++) {
                mean[k] = mean[k] + h->GetBinContent(l);
            }
	    mean[k] = mean[k]/nPts;
        }
    }
  }
}
}