function [R] = myHarrisCorner(Ix, Iy, threshold)
k = 0.04;
newGFilter = fspecial('gaussian',[3 3],3);
IxIy = myImageFilter(Ix.*Iy,newGFilter);
%R = ( ((Ix.^2 .* Iy.^2) ) - (k*((Ix.^2 + Iy.^2).^2)) );
R = zeros(size(Ix));
for i=1:size(Ix,1)
    for j=1:size(Ix,2)
        R(i,j) = ( ((Ix(i,j).^2 .* Iy(i,j).^2) ) - (k*((Ix(i,j).^2 + Iy(i,j).^2).^2)) );
    end
end


% imshow(imread('img05.jpg'));
% hold on;

[row,col] = find(R>threshold);
plot(col,row,'ro');
%plot(100,100,'ro');
% hold off;
