function gf = gfilter(w, theta, sigma, filter_size, filter_type)
EVEN = 0;
%ODD > 0
fbound = int8(filter_size/2-1);
[x,y] = meshgrid(-fbound:fbound, -fbound:fbound);
% radius = double(fbound);
diameter = double(filter_size);
% x = (double(x)/radius)*(2*sigma);
% y = -(double(y)/radius)*(2*sigma);
x = (double(x)/diameter)*(4*sigma);
y = -(double(y)/diameter)*(4*sigma);
sr = x*cos(theta)+y*sin(theta);
if(filter_type==EVEN)
    sf = cos(double(2*pi*w*sr));
else
    sf = sin(double(2*pi*w*sr));
end
g = (1/(2*pi*sigma^2))*exp(-(double(x.^2+y.^2))/(2*sigma^2));
gf = g.*sf;
end