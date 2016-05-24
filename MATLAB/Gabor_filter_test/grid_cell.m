function c = grid_cell(img_h, img_w, row, col)
    if (mod(img_h, row)<=row/2)
        cell_height = floor(img_h/row);
    else
        cell_height = ceil(img_h/row);
    end
    if (mod(img_w, col)<=col/2)
        cell_width = floor(img_w/col);
    else
        cell_width = ceil(img_w/col);
    end
    g_cell = cell(row*col, 1);
%     figure;
    for r = drange(1:row)
        for c = drange(1:col)
            current = (r-1)*col+c;
            from_h = (r-1)*cell_height+1;
            to_h = r*cell_height;
            from_w = (c-1)*cell_width+1;
            to_w = c*cell_width;
            if(r==row)
                to_h = img_h;
            end
            if(c==col)
                to_w = img_w;
            end
            z = zeros(img_h, img_w);
            z(from_h:to_h, from_w:to_w) = 1;
            g_cell{current} = logical(z);
%             subplot(row,col,current);
%             imshow(z);
        end
    end
    c = g_cell;    
end