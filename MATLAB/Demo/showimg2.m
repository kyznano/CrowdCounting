function [] = showimg2(inputdemo, yresult, type, Input_Img_Idx, Input_Img)
    RESIZE = 320;
    TEST = 1;
%     TRAIN = 0;

%     path_list = Img{1};
    idx_list = inputdemo(:,1);
%     groundtruth = Img{2};
    groundtruth = inputdemo(:,2);
	if type == TEST
        for i = (1:length(idx_list))
            f = figure;
            %path = path_list{i};
            path = Input_Img{find(Input_Img_Idx==idx_list(i))};
            gt = groundtruth(i);
            img = imread(path);
            img = imresize(img, [NaN, RESIZE]);
            imshow(img);
            str = {['Estimation = ' num2str(round(yresult(i)))], ['Ground-truth = ' num2str(gt)]};
            annotation('textbox', [0.37 0.05 0.1 0.1], 'String', str);
            figure(f);
        end
    else
        for i = (1:length(path_list))
            f = figure;
            path = path_list{i};
            gt = groundtruth(i);
            img = imread(path);
            img = imresize(img, [NaN, RESIZE]);
            imshow(img);
            annotation('textbox', [0.37 0.03 0.1 0.1], 'String', ['Ground-truth = ' num2str(gt)]);
            figure(f);
        end
    end
end