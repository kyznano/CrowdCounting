function gfs = GaborFilter(winLen,uh,ul,S,D)

% gfs(SCALE, DIRECTION, :, :)

winLen = winLen + mod(winLen, 2) -1;
x0 = (winLen + 1)/2;
y0 = x0;

if S==1
    a = 1;
    su = uh/sqrt(log(4));
    sv = su;
else
    a = (uh/ul)^(1/(S-1));
    su = (a-1)*uh/((a+1)*sqrt(log(4)));
    if D==1
        tang = 1;
    else
        tang = tan(pi/(2*D));
    end
    sv = tang * (uh - log(4)*su^2/uh)/sqrt(log(4) - (log(4)*su/uh)^2);
end

sx = 1/(2*pi*su);
sy = 1/(2*pi*sv);
coef = 1/(2*pi*sx*sy);

gfs = zeros(S, D, winLen, winLen);

for d = 1:D
    theta = (d-1)*pi/D;
    for s = 1:S
        scale = a^(-(s-1));
        gab = zeros(winLen);
        for x = 1:winLen
            for y = 1:winLen
                X = scale * ((x-x0)*cos(theta) + (y-y0)*sin(theta));
                Y = scale * (-(x-x0)*sin(theta) + (y-y0)*cos(theta));
                gab(x, y) = -0.5 * ( (X/sx).^2 + (Y/sy).^2 ) + (2*pi*1j*uh)*X ;
            end
        end
        gfs(s, d, :, :) = scale * coef * exp(gab); 
    end
end