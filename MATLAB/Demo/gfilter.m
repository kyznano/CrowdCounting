function gf = new_gfilter(w, theta, sigma, fsize, ftype)
    EVEN = 0;
    p = floor(fsize/2)/fsize;
    [xr, yr] = meshgrid(-p:1/fsize:p, -p:1/fsize:p);
    yr = -yr;
    rotation = xr*cosd(theta)+yr*sind(theta);
    if(ftype==EVEN)
        bandpass = cosd(double(2*180*w*rotation));
    else
        bandpass = sind(double(2*180*w*rotation));
    end
%     [xg, yg] = meshgrid(-2*sigma:2*sigma/floor(fsize/2):2*sigma, -2*sigma:2*sigma/floor(fsize/2):2*sigma);
%     yg = -yg;
    filter_radius = floor(fsize/2);
    [xg, yg] = meshgrid(-filter_radius:filter_radius, -filter_radius:filter_radius);
    yg = -yg;
    sigma_scale = sigma*(fsize/2);
    A = 1/(2*pi*sigma_scale^2);
    k = -1*((xg.^2+yg.^2)/(2*sigma_scale^2));
    gauss = A*exp(k);
    gabor_filter = gauss.*bandpass;
    gf = gabor_filter;
end