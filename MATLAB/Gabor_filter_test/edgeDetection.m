% return img (0,1)
function oimg = edgeDetection(inputImg)
BoundCoefficient = 4;
edImg = scaleI(imfilter(double(inputImg), fspecial('sobel'), 'symmetric', 'same', 'conv'));
avg = mean(edImg(:));
b = mean(abs(edImg(:)-avg));
b1 = avg-BoundCoefficient*b;
b2 = avg+BoundCoefficient*b;
pArea = double(1-(edImg>b1).*(edImg<b2));
ed = edImg.*pArea;
ed(ed==0) = 1;
oimg = 1 - ed;
end