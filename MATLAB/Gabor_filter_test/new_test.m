freq = [ 2 4 8 16 32 64 ];
angle = [ 0 45 90 135 ];
fsize = 31;
fsize_x = 320*1;
fsize_y = 240*1;
resize = fsize_x;
imgname = 'f1.jpg';

gabor_filter = cell(length(freq), length(angle));
rotation = cell(length(freq), length(angle));
bandpass = cell(length(freq), length(angle));
gauss = cell(length(freq), length(angle));

for i = (1:length(freq))
    for j = (1:length(angle))
        w = freq(i);
        sigma = 1/w;
        theta = angle(j);
        
        t = (i-1)*length(angle)+j;
        
        p = floor(fsize_x/2)/fsize_x;
        q = floor(fsize_y/2)/fsize_y;
%         p = floor(fsize/2)/fsize;
%         [xr, yr] = meshgrid(-p:1/fsize_x:p, -q:1/fsize_y:q);
        [xr, yr] = meshgrid(0:1/fsize_x:1, 0:1/fsize_y:1);
        yr = -yr;
        xr = xr(1:fsize_y,1:fsize_x);
        yr = yr(1:fsize_y,1:fsize_x);
        rotation{i,j} = xr*cosd(theta)+yr*sind(theta);        
        bandpass{i,j} = cosd(double(2*180*w*rotation{i,j}));
        
%         [xg, yg] = meshgrid(-2*sigma:2*sigma/floor(fsize_x/2):2*sigma, -2*sigma:2*sigma/floor(fsize_y/2):2*sigma);
        [xg, yg] = meshgrid(-floor(fsize_x/2):floor(fsize_x/2), -floor(fsize_y/2):floor(fsize_y/2));
        yg = -yg;
        xg = xg(1:fsize_y,1:fsize_x);
        yg = yg(1:fsize_y,1:fsize_x);
        sigma2 = sigma*(resize/2);
        A = 1/(2*pi*sigma2^2);
%         A = 1;
        k = -1*((xg.^2+yg.^2)/(2*sigma2^2));
        gauss{i,j} = A*exp(k);
        
        gabor_filter{i,j} = gauss{i,j}.*bandpass{i,j};
%         gabor_filter{i,j} = bandpass{i,j};
%         subplot(6,4,t);
%         imshow(scaleI(gabor_filter{i,j}));
%         title(t);
    end
end

img = rgb2gray(imread(imgname));
resized_img = imresize(img, [NaN, resize]);
n_img = histeq(resized_img, 255);


% draw frequencies filter
% f = figure;
% local_maxima = zeros(1,length(freq));
% for i = (1:length(freq))
% %     for j = (1:length(angle)) 
% %     central_frequency = abs(fft2(gabor_filter{i,1}, size(n_img,1), size(n_img,2)));
% %     central_frequency = central_frequency(1,1:size(n_img,2)/2);
%     local_maxima(i) = find(central_frequency==max(central_frequency));
%     central_frequency(central_frequency<0.001) = 0;
%     if mod(i,2) == 0
%         plot(1:size(n_img,2)/2,central_frequency(:),'b-', 'LineWidth',1);
%     else
%         plot(1:size(n_img,2)/2,central_frequency(:),'r-', 'LineWidth',1);
%     end
%     hold on;
% %     end
% end
% hold off;
% figure(f);

% ft_1 = fft2(gabor_filter{6,1});
% ft_2 = ifft2(ft_1);
output = cell(length(freq),length(angle));
for i = (1:length(freq))
    for j = (1:length(angle))
        a = abs(fft2(gabor_filter{i,j}, size(n_img,1), size(n_img,2))).*fft2(n_img);
        b = ifft2(a);
        output{i,j} = b;
    end
end

