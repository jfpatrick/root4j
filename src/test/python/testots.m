%
% Matlab code to access Root file of TH1F objects
% Using the root4j package

% Names of the objects are wfe-b where
%   e is the event number
%   b is the board number (1-4)
%
% Each object contains 16000 values,
% these are 16 waveforms of 1000 values each
%

function testots(f)

import org.dianahep.root4j.*;
import org.dianahep.root4j.interfaces.*

file = '/tmp/Artdaq1-simpler.root';

if nargin == 1
    file = f;
end

rfr=RootFileReader(file);

nKeys = rfr.nKeys();
nEvents = nKeys/4;

% Loop over events
for i = 1:nEvents

% Loop over boards
  for j = 1:4

      name=sprintf('wf%d-%d',i,j);
    
      h=rfr.get(name);
      values=h.getArray();
      nMeas = 16;
      nPts = h.getEntries()/nMeas;

% Loop over waveforms
      for k = 1:nMeas

% Loop over points in each waveform and compute mean
        mean(k) = 0.;
        for l = (k-1)*nPts + 1:k*nPts
          mean(k) = mean(k) + values(l);
        end  % End loop over points
        mean(k) = mean(k)/nPts;
      end % End loop over waveforms
  end     % End loop over boards
end       % End loop over events
end