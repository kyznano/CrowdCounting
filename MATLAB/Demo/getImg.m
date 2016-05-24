function [cell_path_gt] = getImg(name_set, gt, fold, range_value)
    cell_path_gt = { name_set{fold}(range_value), gt{fold}(range_value)};
end