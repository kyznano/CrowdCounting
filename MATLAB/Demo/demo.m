% some constants
% FOLD = 2;

RANGE = (1:10);
SHOW_PLOT = false;

if RUN_ESTIMATE
    run('estimate.m');
    TEST_SET = Test_Img;
    TRAIN_SET = Train_Img;
else
%     load
end

% if TYPE_DEMO == TEST    
%     DEMO_GROUNDTRUTH2 = Gt_Y_Testing2;
%     SET = TEST_SET;
% else
%     DEMO_GROUNDTRUTH2 = Gt_Y_Training2;
%     SET = TRAIN_SET;
% end

% Img = getImg(SET, DEMO_GROUNDTRUTH, FOLD, RANGE);
% 
% demoGT = DEMO_GROUNDTRUTH2{FOLD}(RANGE,:);
% demoTest = Xt_Backup{1,FOLD}(RANGE,:);
% demoVal = quicksort_enhance(demoGT',1);
% demoVal = demoVal';

% sort
% quicksort_enhance();

if TYPE_DEMO == TEST
%     ftest = feature_extraction(Img{1}, h_filter, NUMBER_CHANNELS, NUMBER_FEATURES, RESIZED_IMG, GRID_SIZE);
%     ftest = convertCellFeature2Array(ftest);

%     ftest = Xt_Backup{1,FOLD}(RANGE,:);

%     ftest = zeros(size(demoVal,1),size(X_All,2));
%     for i=(1:size(demoVal,1))
%         ftest(i,:) = X_All(find(Input_Img_Idx==demoVal(i,1)),:);
%     end

%     yresult = simlssvm(model{FOLD}, ftest);

%     showimg2(demoVal(:,1:2), yresult, TEST, Input_Img_Idx, Input_Img);
    
    
%     demo_gt = Img{2};

%     demo_gt = demoVal(:,2);
%     demo_ae = abs((round(yresult)-demo_gt));
%     demo_re = abs((round(yresult)-demo_gt))./demo_gt;
%     accuracy = 100-demo_re;
%     figure, stem((1:length(accuracy)),demo_ae, 'MarkerFaceColor','blue', 'MarkerSize',5);
%     xlabel('Frame'); ylabel('Absolute Error');
%     axis([0 length(accuracy)+1 0 max(demo_ae)+1 ]);
%     
%     if RUN_ESTIMATE == false
%         if SHOW_PLOT == true
%             plot_compare(1:number_images, Y_All_S(:,2)', Yt_Result', f_path);
%         end
%     end
else
%     showimg(Img, TRAIN, yresult);
end