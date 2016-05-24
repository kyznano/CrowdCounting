function oimg = backgroundSubtraction(inputImg, bgImg)
BoundCoefficient = 4;
sbg = scaleI(double(inputImg) - double(bgImg));
avg = mean(sbg(:));
b = mean(abs(sbg(:)-avg));
b1 = avg-BoundCoefficient*b;
b2 = avg+BoundCoefficient*b;
e_bg = double(1-(sbg>b1).*(sbg<b2));
sbg2 = sbg.*e_bg;
sbg2(sbg2==0) = 1;
oimg = 1 - sbg2;
end