#
# pyroot code to access Root file of TH1F objects

# Names of the objects are wfe-b where
#   e is the event number
#   b is the board number (1-4)
#
# Each object contains 16000 values,
# these are 16 waveforms of 1000 values each
#

import sys
import ROOT

file = "/tmp/Artdaq1-simpler.root"

if len(sys.argv) > 1:
    file = sys.argv[1]

print "Reading file", file
f = ROOT.TFile(file);

nKey = f.GetNkeys()
nEvents = nKey/4

print "Number of keys: ", nKey, " Number of events: ", nEvents

for i in range(1, nEvents) :
    for j in range(1, 4) :
        name = "wf%d-%d" % (i, j)
        h = f.Get(name)
        
        if h is None :
            print "Record not found for name ", name
        else:

            nMeas = 16
            nPts = h.GetNbinsX()/nMeas

            mean = [0]*nMeas

            for k in range(0, nMeas-1) :
                mean[k] = 0
                for l in range(0, nPts-1) :
                    index = k*nPts + l
                    mean[k] = mean[k] + h.GetBinContent(index)
                mean[k] = mean[k]/nPts