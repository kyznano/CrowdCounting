function feature = feature_extraction(path_list, filter, NUMBER_CHANNELS, NUMBER_FEATURES, RESIZED_IMG, GRID_SIZE)
    feature = cell(length(path_list),1);
    for i = (1:length(path_list))
        path = path_list{i};
        feature{i} = gabor(path, filter, NUMBER_CHANNELS, NUMBER_FEATURES, RESIZED_IMG, str2double(path(20)), GRID_SIZE);
    end
end