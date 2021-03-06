package com.test.admin.conurbations.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.utils.bigImageView.ImagePreview;
import com.test.admin.conurbations.utils.bigImageView.bean.ImageInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouqiong on 2016/12/30.
 */
public abstract class BaseListAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    protected static final int VIEW_TYPE_LOAD_MORE_FOOTER = 100;
    protected boolean isLoadMoreFooterShown;
    public List<T> list;
    protected int mLastPosition = -1;
    private static final int DELAY = 138;

    public Object mContext;

    public BaseListAdapter(Object context) {
        mContext = context;
    }


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LOAD_MORE_FOOTER) {
            return onCreateLoadMoreFooterViewHolder(parent);
        }
        return onCreateNormalViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (isLoadMoreFooterShown && position == getItemCount() - 1) {
            if (holder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
                params.setFullSpan(true);
            }
        }
        if (position < list.size()) {
            T item = list.get(position);
            bindDataToItemView(holder, item);
        }
        //showItemAnim(holder.itemView,position);
        //startAnimator(holder.itemView,position);
    }

    protected abstract void bindDataToItemView(final BaseViewHolder vh, final T item);

    protected View inflateItemView(ViewGroup viewGroup, int layoutId) {
        return inflateItemView(viewGroup, layoutId, false);
    }

    protected View inflateItemView(ViewGroup viewGroup, int layoutId, boolean attach) {
        return LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, attach);
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public List<T> getList() {
        return list;
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size() + (isLoadMoreFooterShown ? 1 : 0);
        } else {
            return (isLoadMoreFooterShown ? 1 : 0);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoadMoreFooterShown && position == getItemCount() - 1) {
            return VIEW_TYPE_LOAD_MORE_FOOTER;
        }
        return getDataViewType(position);
    }

    protected int getDataViewType(int position) {
        return 0;
    }

    public T getItem(int position) {
        return list.get(position);
    }

    protected abstract BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType);

    protected BaseViewHolder onCreateLoadMoreFooterViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_pull_to_refresh_footer, parent, false);
        return new LoadMoreFooterViewHolder(view);
    }

    public void onLoadMoreStateChanged(boolean isShown) {
        this.isLoadMoreFooterShown = isShown;
        if (isShown) {
            notifyItemInserted(getItemCount());
        } else {
            notifyItemRemoved(getItemCount());
        }
    }

    private class LoadMoreFooterViewHolder extends BaseViewHolder {
        public LoadMoreFooterViewHolder(View view) {
            super(view);
        }
    }

    //每个item添加一个动画
    public void showItemAnim(final View view, final int position) {
        final Context context = view.getContext();
        if (position > mLastPosition) {
            view.setAlpha(0);
            view.postDelayed(() -> {
                Animation animation = AnimationUtils.loadAnimation(context,
                        R.anim.slide_in_right);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        view.setAlpha(1);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                view.startAnimation(animation);
            }, DELAY * position);
            mLastPosition = position;
        }
    }

    private void startAnimator(View view, int position) {
        if (position > mLastPosition) {
            view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.item_bottom_in));
            mLastPosition = position;
        }
    }

    public List<ImageInfo> getStringToList(String imgUrl) {
        List<ImageInfo> imageInfoList = new ArrayList<>();
        ImageInfo imageInfo = new ImageInfo();
        imageInfo.setOriginUrl(imgUrl);// 原图
        imageInfo.setThumbnailUrl(imgUrl);// 缩略图，实际使用中，根据需求传入缩略图路径。如果没有缩略图url，可以将两项设置为一样，并隐藏查看原图按钮即可。
        imageInfoList.add(imageInfo);
        return imageInfoList;
    }

    public void startShowImageActivity(View transitView, List<ImageInfo> imageInfoList) {
        ImagePreview
                .getInstance()
                .setContext(transitView.getContext())
                .setIndex(0)
                .setImageInfoList(imageInfoList)
                .setShowDownButton(true)
                .setShowOriginButton(true)
                .setFolderName("BigImageViewDownload")
                .setScaleLevel(1, 3, 8)
                .setZoomTransitionDuration(500)
                .start();
    }
}
