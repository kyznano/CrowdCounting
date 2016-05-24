% freq - centre frequency
% resize - size of image after preprocessing
% gabor_filter - contains 24 Gabor filters (filter size is 31x31)
% features - contains mean and variance of image after filtering, first row contains
%            mean values, second row contains variance values

% freq = [ 0.775 1.55 3.1 6.2 ];
% freq = [ 0.19375 0.3875 0.775 1.55 3.1 6.2 ];
freq = [ 2 4 8 16 32 64 ];
freq = freq / (320/31);
angle = [ 0 45 90 135 ];
fsize = 31;
resize = 320;
thres = 0.05;
imgname = 'f1.jpg';
maskname = 'v1_new.jpg';
bgname = 'bg1.jpg';
gabor_filter = cell(length(freq), length(angle));
rotation = cell(length(freq), length(angle));
bandpass = cell(length(freq), length(angle));
gauss = cell(length(freq), length(angle));
for i = (1:length(freq))
    for j = (1:length(angle))
        w = freq(i);
        sigma = 1/(freq(i));
        theta = angle(j);
        
        t = (i-1)*length(angle)+j;
        
        p = floor(fsize/2)/fsize;
        [xr, yr] = meshgrid(-p:1/fsize:p, -p:1/fsize:p);
%         [xr, yr] = meshgrid(-15:15, -15:15);
        yr = -yr;        
        rotation{i,j} = xr*cosd(theta)+yr*sind(theta);        
        bandpass{i,j} = sind(double(2*180*w*rotation{i,j}));
        
        sigma_new = sigma*(fsize/2);
%         [xg, yg] = meshgrid(-2*sigma:2*sigma/floor(fsize/2):2*sigma, -2*sigma:2*sigma/floor(fsize/2):2*sigma);
        [xg, yg] = meshgrid(-15:15, -15:15);
        yg = -yg;
        A = 1/(2*pi*sigma_new^2);
        k = -1*((xg.^2+yg.^2)/(2*sigma_new^2));
        gauss{i,j} = A*exp(k);
        
        gabor_filter{i,j} = gauss{i,j}.*bandpass{i,j};
        subplot(6,4,t);
        imshow(scaleI(gabor_filter{i,j}));
        title(t);
    end
end

features = zeros(2, 24);
fimg = cell(24, 1);
fbg = cell(24, 1);
fam = cell(24,1);
fam2 = cell(24,1);
img = rgb2gray(imread(imgname));
resized_img = imresize(img, [NaN, resize]);
% n_img = resized_img;
n_img = histeq(resized_img, 255);
bg = rgb2gray(imread(bgname));
bg = imresize(bg, [NaN, resize]);
% bg = histeq(bg, 255);
sbg = scaleI(double(n_img) - double(bg));
avg = mean(sbg(:));
b = mean(abs(sbg(:)-avg));
b1 = avg-4*b;
b2 = avg+4*b;
e_bg = double(1-(sbg>b1).*(sbg<b2));
sbg2 = sbg.*e_bg;
sbg2(sbg2==0) = 1;
sbg2 = 1 - sbg2;
n_img = im2double(n_img);
% figure, imshow(n_img);
mask = imread(maskname);
mask = imresize(mask, [NaN, resize]);
mask = im2bw(mask, 0.5);
mask_uint8 = uint8(mask);
cell_list = grid_cell(size(n_img,1), size(n_img,2), 20, 40);
j = 1;
positive_mask = zeros(1, size(cell_list,1));
for i = drange(1:size(cell_list,1))
    mask_local = mask_uint8(cell_list{i});
    bg_local = sbg2(cell_list{i});
    if mean(mask_local)>0 && mean(bg_local)>0
        if sum(bg_local>0)/length(bg_local)> thres
            positive_mask(j) = i;
            j = j+1;
        end
    end
end
npositive = sum(positive_mask>0);
feature = zeros(2, 24*npositive);
% figure, imshow(bg);
sobelx = fspecial('sobel');
sobely = sobelx';

for i = (1:6)
    for j = (1:4)
        t = (i-1)*4+j;            
        fbg{t} = imfilter(sbg, gabor_filter{i,j}, 'symmetric', 'same', 'conv');
        fimg{t} = imfilter(n_img, gabor_filter{i,j}, 'symmetric', 'same', 'conv');
        fx = imfilter(fimg{t}, sobelx, 'symmetric', 'same', 'conv');
        fy = imfilter(fimg{t}, sobely, 'symmetric', 'same', 'conv');
        fam2{t} = sqrt(fx.^2+fy.^2);
        fx = scaleI(fx);
        fy = scaleI(fy);
        amplitude = sqrt(fx.^2+fy.^2);
        fam{t} = amplitude;
%         figure, imshow(scaleI(fbg{t}));
        % --------
%         img_filtered = imfilter(im2double(n_img), gabor_filter{i,j}, 'symmetric', 'same', 'conv');
%         img_mask = fimg{t}.*double(mask_uint8);
%         figure, imshow(img_mask);
        q = 1;
        while positive_mask(q)>0
            img_local = fimg{t}(cell_list{positive_mask(q)});
            cur = (t-1)*npositive+q;
            for f = drange(1:2)
                switch f
                    case 1
                        feature(f,cur) = mean(img_local(:));
                    case 2
                        feature(f,cur) = var(img_local(:));
                    otherwise
                end
            end
            q = q+1;
        end
        % --------
    end
end
% t = abs(n_img-bg);
% figure, imshow(t);
% show image filtered by Gabor filter no.22
% figure, imshow(scaleImgIntensity(fimg{22}));
q = 1;
% n_img = im2double(n_img);
pm = zeros(240,320);
while positive_mask(q)>0
    pm = pm + cell_list{positive_mask(q)};
    q = q+1;
end

ttimg = imfilter(n_img, gabor_filter{1,1}, 'symmetric', 'same', 'conv');
ttimg = scaleI(ttimg);
% figure, imshow(ttimg);
otimg = ttimg.*double(mask_uint8);
% figure, imshow(otimg);

j = 1;
positive_mask = zeros(1, size(cell_list,1));
for i = drange(1:size(cell_list,1))
    mask_local = mask_uint8(cell_list{i});
    if mean(mask_local)>0
        positive_mask(j) = i;
        j = j+1;
    end
end
npositive = sum(positive_mask>0);
feature = zeros(2, npositive);

sobelx = fspecial('sobel');
sobely = sobelx';


img_filtered = imfilter(im2double(n_img), gabor_filter{1,1}, 'symmetric', 'same', 'conv');
% gradient
img_sobelx = imfilter(img_filtered, sobelx, 'symmetric', 'same', 'conv');
img_sobely = imfilter(img_filtered, sobely, 'symmetric', 'same', 'conv');
amplitude = sqrt(img_sobelx.^2+img_sobely.^2);
img_filtered = amplitude;
% -- gradient
img_mask = img_filtered;
%     img_mask = img_filtered.*double(mask_uint8);
j = 1;
while positive_mask(j)>0
    img_local = img_mask(cell_list{positive_mask(j)});
    cur = j;
    for i = drange(1:2)
        switch i
            case 1
                feature(i,cur) = mean(img_local(:));
            case 2
                feature(i,cur) = var(img_local(:));
            otherwise
        end
    end
    j = j+1;
end


feature = feature(:);
feature = feature';
f = feature;
    
pts = reshape(f, 2, npositive);
% [centroid , assignment] = vl_kmeans(pts, 4, 'verbose', 'distance', 'l1', 'algorithm','elkan');
% figure, scatter(pts(1,:), pts(2,:), 'b.');
% hold on;

% scatter(centroid(1,:), centroid(2,:), 'r.', 'LineWidth', 5);

% figure, imshow(n_img.*pm);

% draw frequencies filter
f = figure;
local_maxima = zeros(1,length(freq));
for i = (1:length(freq))
%     for j = (1:length(angle)) 
    central_frequency = abs(fft2(gabor_filter{i,1}, size(n_img,1), size(n_img,2)));
    central_frequency = central_frequency(1,1:size(n_img,2)/2);
    local_maxima(i) = find(central_frequency==max(central_frequency));
    central_frequency(central_frequency<0.001) = 0;
    if mod(i,2) == 0
        plot(1:size(n_img,2)/2,central_frequency(:),'b-', 'LineWidth',1);
    else
        plot(1:size(n_img,2)/2,central_frequency(:),'r-', 'LineWidth',1);
    end
    hold on;
%     end
end
hold off;
figure(f);