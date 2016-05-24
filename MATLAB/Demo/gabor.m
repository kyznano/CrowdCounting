% this function is used for extracting features
function f = gabor(imgname, filters, nchannels, nfeatures, resize, view, grid_size)
%preprocessing
img = rgb2gray(imread(imgname));
resized_img = imresize(img, [NaN, resize]);
n_img = histeq(resized_img, 255);
switch view
    case 1
        mask_path = 'masks/mask_V1.jpg';
    case 2
        mask_path = 'masks/mask_V2.jpg';
    case 3
        mask_path = 'masks/mask_V3.jpg';
    case 4
        mask_path = 'masks/mask_V4.jpg';
    otherwise
end
% mask creation
mask = imread(mask_path);
mask = imresize(mask, [NaN, resize]);
mask = im2bw(mask, 0.5);
mask_uint8 = double(uint8(mask));
cell_list = grid_cell(size(n_img,1), size(n_img,2), grid_size(1), grid_size(2));
feature = zeros(nfeatures, nchannels*size(cell_list,1));
% figure;
for k = drange(1:nchannels)
    filtered_img = imfilter(double(n_img), filters{k}, 'symmetric', 'same', 'conv');
    img_mask = filtered_img.*mask_uint8;
    for i = drange(1:size(cell_list,1))
        img_local = img_mask(cell_list{i});
        cur = (k-1)*size(cell_list,1)+i;
        for f = drange(1:nfeatures)
            switch f
                case 1
                    feature(f,cur) = mean(img_local(:));
                case 2
                    feature(f,cur) = var(img_local(:));
                otherwise
            end
        end
    end    
end
f = feature(:)';
end