function [] = plot_compare(x, y_groundtruth, y_test, folder_path)
    FRAMES_PER_FIGURE = 40;
    nframe = length(x);
    block = uint16(nframe/FRAMES_PER_FIGURE);
    if(block*FRAMES_PER_FIGURE<nframe)
        block = block + 1;
        FRAMES_PER_FIGURE = ceil(nframe/block);
    end
    for b = drange(1:block)
        start = (b-1)*FRAMES_PER_FIGURE+1;
        finish = b*FRAMES_PER_FIGURE;
        if(b==block)
            finish = nframe;
        end
        f = figure;
        x = start:finish;
        y = y_groundtruth(start:finish);
        m = max(y);
        plot(x,y,'b.-', 'LineWidth',2, 'MarkerSize',15);
        hold all;
        y = uint16(y_test(start:finish));
        if(max(y)>m)
            m = max(y);
        end
        plot(x,y,'r.-', 'LineWidth',2, 'MarkerSize',15);
        hleg = legend('Ground-truth','Estimation',...
              'Location','SouthOutside');
        set(hleg,'FontAngle','italic','TextColor',[.3,.2,.1]);
%         set(gcf,'Visible','off');
        % title('The ground truth count and estimation. Blue line = ground truth, Red line = estimation');
        xlabel('Frame'); ylabel('Number of people');
        axis([start-1 finish+1 0 m+3]);        
        file_name = strcat('figure_', int2str(b));
        saveas(f, strcat(folder_path,file_name), 'jpg');
    end
end