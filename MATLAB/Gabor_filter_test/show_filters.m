w = [2 4 8 16 32 64];
theta = [0 pi/4 pi/2 3*pi/4];
FILTER_SIZE = 31;
CONST_HE = 0;
CONST_HO = 1;
% Gabor filter design
eh_filter = cell(length(w)*length(theta));
oh_filter = cell(length(w)*length(theta));
for i = drange(1:length(w))
    for j = drange(1:length(theta))
        eh_filter{j+(i-1)*length(theta)} = gfilter(w(i), theta(j), 1/w(i), FILTER_SIZE, CONST_HE);
        oh_filter{j+(i-1)*length(theta)} = gfilter(w(i), theta(j), 1/w(i), FILTER_SIZE, CONST_HO);
    end
end

figure;
for k = drange(1:length(w)*length(theta)) 
    u3 = eh_filter{k};
    %a = min(u3(:));
    %u4 = u3 - a;
    %b = max(u4(:));
    %u4 = u4/b;
    %u4 = u4*255;
    %figure, imshow(uint8(u4));
    subplot(6,4,k);
    imshow(u3);
    %imshow(u3, []);
    title(k);
end

% figure;
% for k = drange(1:24) 
%     u3 = eh_filter{k};
%     a = min(u3(:));
%     u3 = u3 - a;
%     b = max(u3(:));
%     u3 = u3/b;
%     u3 = u3*255;
%     subplot(6,4,k);
%     imshow(u3);
%     title(k);
% end

%THRES = 10000;
%co = 1;
%for k = drange(1:15)
%for k = drange(1:size(eh_filter, 1))
%    f = eh_filter{k};
%    ind = find(f == 0);
%    f(ind) = -1;
%    pos = find(f > 0);
%    sm_f = f(pos);
%    a0 = min(sm_f);
%    f(ind) = a0;    
%    log_f = log(f);     
%    a = min(log_f(:));
%    m_abs_f = log_f - a;
%    b = max(m_abs_f(:));
%    m_abs_f = m_abs_f/b;
%    abs_f = abs(m_abs_f);
%    abs_f(:) = 1-abs_f(:);
%    figure, imshow(abs_f);
%end