new_statistic = zeros(NTEST, NSTATISTIC);
for i = drange(1:NTEST)
    Ygt = Y_Backup{i}(:,2);
    Yt = Yt_Backup{i};
    ae = abs(round(Yt)-Ygt);
    re = (double(ae)./double(Ygt))*100;
    for e = drange(1:NSTATISTIC)
        switch e
            case 1  % mean absolute error
                new_statistic(i,e) = mean(ae);
            case 2  % max ae
                new_statistic(i,e) = max(ae);                                   
            case 3  % var ae
                new_statistic(i,e) = var(ae);
            case 4  % mean relative error
                new_statistic(i,e) = mean(re);
            case 5  % max re
                new_statistic(i,e) = max(re);
            case 6  % var re
                new_statistic(i,e) = var(re);
            otherwise
        end
    end
end
statistic = new_statistic;