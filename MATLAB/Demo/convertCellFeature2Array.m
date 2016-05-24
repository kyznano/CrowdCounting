function r = convertCellFeature2Array(c)
    rowCell = size(c,1);
    colCell = size(c{1},2);
    f = zeros(rowCell, colCell);
    for i=(1:rowCell)
        f(i,:) = c{i};
    end
    r = f;
end