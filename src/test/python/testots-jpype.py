#
# python code to access Root file of TH1F objects
# Using the root4j package via jpype

# Names of the objects are wfe-b where
#   e is the event number
#   b is the board number (1-4)
#
# Each object contains 16000 values,
# these are 16 waveforms of 1000 values each
#

import jpype
from jpype import *
import numpy as np

jvmPath = jpype.getDefaultJVMPath()
jpype.startJVM(jvmPath, "-Djava.class.path=root4j.jar", "-Djpype=true")
print java.lang.System.getProperty("java.class.path")

z = JPackage("org").dianahep.root4j

file = "/tmp/Artdaq1-simpler.root"
if len(sys.argv) > 1:
    file = sys.argv[1]

print "Reading file", file
f = z.RootFileReader(file)

nKey = f.nKeys()
nEvents = nKey/4

print "Number of keys: ", nKey, " Number of events: ", nEvents

for i in range(1, nEvents) :
    for j in range(1, 4) :
        name = "wf%d-%d" % (i, j)
        h = f.get(name)
        
        if h is None :
            print "Record not found for name ", name
        else :

            nMeas = 16
            values = h.getArray()
            entries = h.getEntries()
            nPts = int(entries/nMeas)

            mean = [0]*nMeas

            for k in range(0, nMeas-1) :
                mean[k] = 0
                for l in range(0, nPts-1) :
                    index = k*nPts + l
                    mean[k] = mean[k] + values[index]
                mean[k] = mean[k]/nPts