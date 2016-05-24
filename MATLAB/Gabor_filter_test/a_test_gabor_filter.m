w = 64;
sigma = 1/w;
theta = pi/4;
filter_size = 31;
filter_type = 0;
EVEN = 0;
%ODD = 1;
fbound = int8(filter_size/2-1);
[x,y] = meshgrid(-fbound:fbound, -fbound:fbound);
x = double(x)/15*(2*sigma);
y = -double(y)/15*(2*sigma);
sr = x*cos(theta)+y*sin(theta);
if(filter_type==EVEN)
    sf = cos(double(2*pi*w*sr));
    %figure, imshow(sf);
else
    sf = sin(double(2*pi*w*sr));
    %figure, imshow(sf);
end
g = 1/(2*pi*(sigma^2))*exp(-(x.^2+y.^2)/(2*sigma^2));
%figure, imshow(g);
gf = g.*sf;