% freq - centre frequency
% resize - size of image after preprocessing
% gabor_filter - contains 24 Gabor filters (filter size is 31x31)
% features - contains mean and variance of image after filtering, first row contains
%            mean values, second row contains variance values

freq = [ 2 4 8 16 32 64 ];
angle = [ 0 45 90 135 ];
fsize = 31;
resize = 320;
grid = [16 32];
thres = 0.05;
imgname = 'f4.jpg';
maskname = 'v1.jpg';
bgname = 'bg1.jpg';
gabor_filter = cell(length(freq), length(angle));
rotation = cell(length(freq), length(angle));
bandpass = cell(length(freq), length(angle));
gauss = cell(length(freq), length(angle));
for i = (1:length(freq))
    for j = (1:length(angle))
        w = freq(i);
        sigma = 1/w;
        theta = angle(j);
        
        p = floor(fsize/2)/fsize;
        [xr, yr] = meshgrid(-p:1/fsize:p, -p:1/fsize:p);
        yr = -yr;        
        rotation{i,j} = xr*cosd(theta)+yr*sind(theta);        
        bandpass{i,j} = cosd(double(2*180*w*rotation{i,j}));
        
        [xg, yg] = meshgrid(-2*sigma:2*sigma/floor(fsize/2):2*sigma, -2*sigma:2*sigma/floor(fsize/2):2*sigma);
        yg = -yg;
        A = 1/(2*pi*sigma^2);
        k = -1*((xg.^2+yg.^2)/(2*sigma^2));
        gauss{i,j} = A*exp(k);
        
        gabor_filter{i,j} = gauss{i,j}.*bandpass{i,j};
    end
end

features = zeros(2, 24);
fimg = cell(24, 1);
fbg = cell(24, 1);
img = rgb2gray(imread(imgname));
resized_img = imresize(img, [NaN, resize]);
n_img = histeq(resized_img, 255);
bg = rgb2gray(imread(bgname));
bg = imresize(bg, [NaN, resize]);
bg = histeq(bg, 255);

gg_bg = imfilter(double(bg), fspecial('gaussian'), 'symmetric', 'same', 'conv');
m_img = imfilter(double(n_img), fspecial('gaussian'), 'symmetric', 'same', 'conv');
% figure, imshow(scaleI(m_img));

eimg = edgeDetection(m_img);
ebg = edgeDetection(gg_bg);
sImgBg = backgroundSubtraction(ebg, eimg);
% figure, imshow(eimg);
sbg2 = backgroundSubtraction(m_img, gg_bg);
% figure, imshow(sbg2);
n_img = im2double(n_img);

eBg = edgeDetection(bg);
% figure, imshow(eBg);


figure, imshow(scaleI(m_img));
% hold on;            %# Add subsequent plots to the image
% plot(50,10,'yo');  %# NOTE: x_p and y_p are switched (see note below)!
% hold off;           %# Any subsequent plotting will overwrite the image!

mask = imread(maskname);
mask = imresize(mask, [NaN, resize]);
mask = im2bw(mask, 0.5);
mask_uint8 = uint8(mask);
cell_list = grid_cell(size(n_img,1), size(n_img,2), grid(1), grid(2));
j = 1;
positive_mask = zeros(1, size(cell_list,1));
for i = drange(1:size(cell_list,1))
    mask_local = mask_uint8(cell_list{i});
    bg_local = sbg2(cell_list{i});
    so_local = eimg(cell_list{i});
%     if mean(mask_local)>0 && mean(bg_local)>0
    if mean(mask_local)>0 && mean(so_local)>0 && mean(bg_local)>0
        if sum(so_local>0)/length(so_local)> thres && sum(bg_local>0)/length(bg_local)> thres
            positive_mask(j) = i;
            j = j+1;
        end
    end
end
npositive = sum(positive_mask>0);
feature = zeros(2, 24*npositive);
% figure, imshow(bg);
for i = (1:6)
    for j = (1:4)
        t = (i-1)*4+j;            
%         fbg{t} = imfilter(sbg, gabor_filter{i,j}, 'symmetric', 'same', 'conv');
        fimg{t} = imfilter(n_img, gabor_filter{i,j}, 'symmetric', 'same', 'conv'); 
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
% so = fspecial('sobel');
% so_img = imfilter(n_img, so, 'symmetric', 'same', 'conv');
% figure, imshow(so2);
figure, imshow(n_img.*pm);