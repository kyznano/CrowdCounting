%CONSTANTS
CONST_HE = 0;
CONST_HO = 1;
CONST_EVEN_ODD = 3;
% GT = load('../ground_truth/PETS2009_ground_truth');
%w = [2 4 8 16 32 64];
w = [0.19375 0.3875 0.775 1.55 3.1 6.2];
theta = [0 45 90 135];
RESIZED_IMG = 320;
FILTER_SIZE = 31;
NUMBER_FEATURES = 2;
NUMBER_CHANNELS = length(w)*length(theta);
NSTATISTIC = 6;
NSTATISTIC_DATASET = 4;
%INIT
filter_type = CONST_HO;
% KFOLD = 3;
NTEST = 1;
% GRID_SIZE = [3 4];
NSET = 1;
% DS_NAME = cell(NSET,1);
FOLDER_FIGURES_PATH = strcat('result/demo_version_new/',DS_NAME{1});
% DS_NAME{1} = 'S1L1_1_V1';
% DS_NAME{2} = 'S1L1_1_V2';
% DS_NAME{3} = 'S1L1_1_V3';
% DS_NAME{1} = 'S1L1_2_V1';
% DS_NAME{5} = 'S1L1_2_V2';
% DS_NAME{6} = 'S1L1_2_V3';
% DS_NAME{1} = 'S1L2_1_V1';
% DS_NAME{8} = 'S1L2_1_V2';
% DS_NAME{9} = 'S1L2_1_V3';
% DS_NAME{1} = 'S1L3_1_V1';
% DS_NAME{11} = 'S1L3_1_V2';
% DS_NAME{1} = 'S1L3_1_V3';
% DATASET_GROUNDTRUTH = cell(NSET,1);
% DATASET_GROUNDTRUTH{1} = GT.S1L1_1_V1;
% DATASET_GROUNDTRUTH{2} = GT.S1L1_1_V2;
% DATASET_GROUNDTRUTH{3} = GT.S1L1_1_V3;
% DATASET_GROUNDTRUTH{1} = GT.S1L1_2_V1;
% DATASET_GROUNDTRUTH{5} = GT.S1L1_2_V2;
% DATASET_GROUNDTRUTH{6} = GT.S1L1_2_V3;
% DATASET_GROUNDTRUTH{1} = GT.S1L2_1_V1;
% DATASET_GROUNDTRUTH{8} = GT.S1L2_1_V2;
% DATASET_GROUNDTRUTH{9} = GT.S1L2_1_V3;
% DATASET_GROUNDTRUTH{1} = GT.S1L3_1_V1;
% DATASET_GROUNDTRUTH{11} = GT.S1L3_1_V2;
% DATASET_GROUNDTRUTH{1} = GT.S1L3_1_V3;

% some additional constant
FEATURE_DIMENSION = NUMBER_CHANNELS*NUMBER_FEATURES*GRID_SIZE(1)*GRID_SIZE(2);

% Gabor filter design
if(filter_type==CONST_EVEN_ODD)
    eh_filter = cell(length(w)*length(theta),1);
    oh_filter = cell(length(w)*length(theta),1);
    for i = drange(1:length(w))
        for j = drange(1:length(theta))
            eh_filter{j+(i-1)*length(theta)} = gfilter(w(i), theta(j), 1/w(i), FILTER_SIZE, CONST_HE);
            oh_filter{j+(i-1)*length(theta)} = gfilter(w(i), theta(j), 1/w(i), FILTER_SIZE, CONST_HO);
        end
    end
else
    h_filter = cell(length(w)*length(theta),1);
    for i = drange(1:length(w))
        for j = drange(1:length(theta))
            h_filter{j+(i-1)*length(theta)} = gfilter(w(i), theta(j), 1/w(i), FILTER_SIZE, filter_type);
        end
    end
end
% MACHINE LEARNING
% INITIALIZE VARIABLES
number_images = 0;
images_set = meshgrid(1:NSET,1);
for e = drange(1:NSET)
    images_set(e) = size(DATASET_GROUNDTRUTH{e},1);
    number_images = number_images + images_set(e);    
end

one_fold = floor(number_images/KFOLD);
number_images = one_fold*KFOLD;

X_All = zeros(number_images, FEATURE_DIMENSION);
Y_All = zeros(number_images, size(DATASET_GROUNDTRUTH{1},2));
Input_Img = cell(number_images,1);
Input_Img_Idx = zeros(number_images,1);
ae_all = meshgrid(1:one_fold, 1:KFOLD);
re_all = meshgrid(1:one_fold, 1:KFOLD);
% FEATURE EXTRACTION (ALL IMAGES IN THE SET)
current_set_backup = 0;
current_set = 0;
t = 0;
from = 0;
delta = 0;
for f = drange(1:number_images)
    while(f>current_set)
        current_set_backup = current_set;
        current_set = current_set + images_set(t+1);
        t = t + 1;
        from = min(DATASET_GROUNDTRUTH{t}(:,1));
        delta = 1-from;
    end   
    pos = f - current_set_backup;
    if(pos-delta<10)
        image_path = strcat('../dataset/',DS_NAME{t},'/frame_000', int2str(pos-delta), '.jpg');
    elseif(pos-delta<100)
        image_path = strcat('../dataset/',DS_NAME{t},'/frame_00', int2str(pos-delta), '.jpg');
    elseif(pos-delta<1000)
        image_path = strcat('../dataset/',DS_NAME{t},'/frame_0', int2str(pos-delta), '.jpg');
    end
    Input_Img_Idx(f) = pos;
    Input_Img_Raw{f} = image_path;
    Input_Img{f} = image_path;
    if(filter_type==CONST_EVEN_ODD)
        fts = gabor_even_odd(image_path, eh_filter, oh_filter, NUMBER_CHANNELS, NUMBER_FEATURES, RESIZED_IMG, str2double(DS_NAME{t}(size(DS_NAME{t},2))), GRID_SIZE);
    else
        fts = gabor(image_path, h_filter, NUMBER_CHANNELS, NUMBER_FEATURES, RESIZED_IMG, str2double(DS_NAME{t}(size(DS_NAME{t},2))), GRID_SIZE);
    end
    X_All(f,:) = fts(:)';
    Y_All(f,:) = DATASET_GROUNDTRUTH{t}(pos,:);
end

statistic_dataset = meshgrid(1:NSTATISTIC_DATASET,1);
for f = drange(1:NSTATISTIC_DATASET)
    switch f
        case 1
            statistic_dataset(f) = min(Y_All(:,2));
        case 2
            statistic_dataset(f) = max(Y_All(:,2));
        case 3
            statistic_dataset(f) = mean(Y_All(:,2));
        case 4
            statistic_dataset(f) = var(Y_All(:,2));
        otherwise
    end
end

statistic = meshgrid(1:NSTATISTIC, 1:NTEST);
X_Backup = cell(NTEST, 1);
Y_Backup = cell(NTEST, 1);
Yt_Backup = cell(NTEST, 1);
Xt_Backup = cell(NTEST, KFOLD);
for f = drange(1:NTEST)
    if(filter_type==CONST_HE)
        f_path = strcat(FOLDER_FIGURES_PATH, '_GRIDSIZE_', int2str(GRID_SIZE(1)), '_', int2str(GRID_SIZE(2)), '_even_kfold_', int2str(KFOLD),'_test_', int2str(f),'/');
    elseif(filter_type==CONST_HO)
        f_path = strcat(FOLDER_FIGURES_PATH, '_GRIDSIZE_', int2str(GRID_SIZE(1)), '_', int2str(GRID_SIZE(2)), '_odd_kfold_', int2str(KFOLD),'_test_', int2str(f),'/');
    else
        f_path = strcat(FOLDER_FIGURES_PATH, '_GRIDSIZE_', int2str(GRID_SIZE(1)), '_', int2str(GRID_SIZE(2)), '_even_odd_kfold_', int2str(KFOLD),'_test_', int2str(f),'/');
    end    
% shuffle
[Input_Img, X_All_S, Y_All_S] = shuf3(Input_Img, X_All, Y_All);
X_Backup{f} = X_All_S;
Y_Backup{f} = Y_All_S;
delta = 0;
Yt_Result = zeros(size(Y_All_S,1), 1);
% LOOP
model = cell(KFOLD,1);
Test_Img = cell(KFOLD,1);
Train_Img = cell(KFOLD,1);
Gt_Y_Testing = cell(KFOLD,1);
Gt_Y_Training = cell(KFOLD,1);
Gt_Y_Testing2 = cell(KFOLD,1);
Gt_Y_Training2 = cell(KFOLD,1);
for k = drange(0:KFOLD-1)
    test_from = 1+k*one_fold;
    test_to = (k+1)*one_fold;
    X_Testing = X_All_S(test_from:test_to,:);
    Y_Testing = Y_All_S(test_from:test_to,:);
    Test_Img{k+1} = Input_Img(test_from:test_to);
    Gt_Y_Testing{k+1} = Y_Testing(:,2);
    Gt_Y_Testing2{k+1} = Y_Testing;
    X_Training = zeros((KFOLD-1)*one_fold, FEATURE_DIMENSION);
    Y_Training = zeros((KFOLD-1)*one_fold, size(DATASET_GROUNDTRUTH{1},2)); 
    Timg = cell((KFOLD-1)*one_fold, 1);
    count_training = 1;
    for t = drange(1:number_images)
        if(t < test_from || t > test_to)     % if not in Y_Testing
            X_Training(count_training,:)=X_All_S(t,:);
            Y_Training(count_training,:)=Y_All_S(t,:);
            Timg{count_training} = Input_Img{t};            
            count_training = count_training + 1;
        end
    end
    Train_Img{k+1} = Timg;    
    Gt_Y_Training{k+1} = Y_Training(:,2);
    Gt_Y_Training2{k+1} = Y_Training;
    type = 'function estimation';
    kernel = 'RBF_kernel';
    %gam = 10;
    %sig2 = 0.2;
    model{k+1} = initlssvm(X_Training, Y_Training(:,2), type, [], [], kernel);
    model{k+1} = tunelssvm(model{k+1}, 'gridsearch', 'crossvalidatelssvm', {10,'mse'});
    model{k+1} = trainlssvm(model{k+1});
    Yt = simlssvm(model{k+1}, X_Testing);
    Yt_Result(test_from:test_to,1) = Yt;
    % Calculate MAE & MRE    
    ae = abs(round(Yt)-Y_Testing(:,2));
    re = ae./Y_Testing(:,2)*100;
    ae_all(k+1,:) = ae;
    re_all(k+1,:) = re;
    Xt_Backup{f,k+1} = X_Testing;
end
Yt_Backup{f} = Yt_Result;
for e = drange(1:NSTATISTIC)
    switch e
        case 1  % mean absolute error
            statistic(f,e) = mean(ae_all(:));
        case 2  % max ae
            statistic(f,e) = max(ae_all(:));
        case 3  % var ae
            statistic(f,e) = var(ae_all(:));
        case 4  % mean relative error
            statistic(f,e) = mean(re_all(:));
        case 5  % max re
            statistic(f,e) = max(re_all(:));
        case 6  % var re
            statistic(f,e) = var(re_all(:));
        otherwise
    end
end
accuracy = 100 - statistic(1,4);
mkdir(f_path);            
% plot_compare(1:number_images, Y_All_S(:,2)', Yt_Result', f_path);
save(strcat(f_path,'log_data'));
end