function [img1] = myImageFilter(img0, h)
% if(ischar(img0))
%     imageDouble = im2double(imread(img0));
% else
%     imageDouble = im2double(img0);
% end
% if(size(imageDouble,3)==3)
%     imageDouble = rgb2gray(imageDouble);
% end

if(ischar(img0))
    img0 = imread(img0);
end
if(size(img0,3)==3)
    img0 = rgb2gray(img0);
end
imageDouble = im2double(img0);

xpad = floor(size(h,1)/2);
ypad = floor(size(h,2)/2);
pad = [xpad ypad];
img1 = zeros(size(imageDouble));
flipKernal = flipud(fliplr(h));
zeroImage = padarray(imageDouble,pad);

for i = (1+xpad):(size(zeroImage,1)-xpad)
    for j = (1+ypad):(size(zeroImage,2)-ypad)
        sumTotal = 0;
        area = zeroImage((i-xpad):((i-xpad)+ size(h,1)-1),( (j-ypad):((j-ypad) + size(h,2)-1)));
        sumTotal = sum(sum((area.*flipKernal)));
        img1(i,j) = sumTotal;
    end
end
        
        

%iVector = imageDouble(:);
%kernal = h(:);
% Looping for each value in imageVector
%for i = 1:size(iVector,2)
 %   totalCount = 0;
  %  for j = 1:size(kernal,2)
   %     totalCount = totalCount + (iVector(i) * kernal(j));
    %end
    %img1(i) = totalCount;
%end
    