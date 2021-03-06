package com.mindmade.mcom.adapterclasses;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mindmade.mcom.R;
import com.mindmade.mcom.activity.ProductDescriptionActivity;
import com.mindmade.mcom.utilclasses.CartSQLiteHelper;
import com.mindmade.mcom.utilclasses.Const;
import com.mindmade.mcom.utilclasses.model.CartProduct;
import com.mindmade.mcom.utilclasses.model.CategoryModel;
import com.mindmade.mcom.utilclasses.model.ProductDescription;
import com.mindmade.mcom.utilclasses.model.ProductModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mindmade technologies on 06-05-2017.
 */
public class ProductsAdapter extends RecyclerView.Adapter {
    public final int TYPE_PRODUCT = 0;
    public final int TYPE_CATEGORY = 0;
    public final int TYPE_LOAD = 1;
    private Activity mContext;
    private List<ProductModel.Products> data;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;
    boolean like;
    CartSQLiteHelper sqLiteHelper;

    public ProductsAdapter(Activity context, ArrayList<ProductModel.Products> passData) {
        mContext = context;
        data = passData;
        sqLiteHelper = new CartSQLiteHelper(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (viewType == TYPE_PRODUCT) {
            return new ProductViewHolder(inflater.inflate(R.layout.product_adapter, parent, false));
        } else if (viewType == TYPE_LOAD) {
            return new LoadHolder(inflater.inflate(R.layout.bottom_loading, parent, false));
        } else {
            return new NoDataHolder(inflater.inflate(R.layout.no_data_layout, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ProductsAdapter.ProductViewHolder) {
            //  Log.w("Success", "Data::: " + data.get(position).getResult().get("src"));

            Log.w("Success", "Data::: " + data.get(position).getName());
            Log.w("Success", "Data::: " + data.get(position).getId());
            Log.d("success", "DDD" + data.get(position).getImage().getProduct_id());
            ((ProductsAdapter.ProductViewHolder) holder).productNameTV.setText(data.get(position).getName());
            ((ProductsAdapter.ProductViewHolder) holder).productOfferPriceTV.setText(data.get(position).getVaraiants().get(0).getPrice());

        //    Glide.with(mContext).load(data.get(position).getImage().getSrc()).diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(R.drawable.placeholder).into(((ProductsAdapter.ProductViewHolder) holder).productImageview);

            Glide
                    .with(mContext)
                    .load(data.get(position).getImage().getSrc())
                    .placeholder(R.drawable.placeholder)
                    .crossFade()
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.placeholder)
                    .into(((ProductsAdapter.ProductViewHolder) holder).productImageview);

            if (data.get(position).isCartCheck()) {
                ((ProductsAdapter.ProductViewHolder) holder).productlikeImage.setImageResource(R.drawable.like_red);
            } else {
                ((ProductsAdapter.ProductViewHolder) holder).productlikeImage.setImageResource(R.drawable.like_gray);
            }

            if (position >= getItemCount() - 1 && isMoreDataAvailable && !isLoading && loadMoreListener != null) {
                isLoading = true;
                loadMoreListener.onLoadMore();
            }
            if (getItemViewType(position) == TYPE_PRODUCT) {
                ((ProductViewHolder) holder).getClass();
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
//        if (data.get(position).equals(Const.LATERST_TYPE_VALUE) || data.get(position).getType().equals(Const.CATEGORY_TYPE_VALUE)) {
//            return TYPE_PRODUCT;
//        } else if (data.get(position).getType().equals(Const.LOAD_TYPE_VALUE)) {
//            return TYPE_LOAD;
//        } else {
//            return TYPE_NO_DATA;
//        }
        return 0;

    }

    public void notifyDataChanged() {
        notifyDataSetChanged();
        isLoading = false;
    }

    public void setMoreDataAvailable(boolean moreDataAvailable) {
        isMoreDataAvailable = moreDataAvailable;
    }

    public boolean isLoading() {
        return isLoading;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
        Log.d("Success", "Comes setLoadMoreListener");
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView productImageview;
        TextView productNameTV, productOfferPriceTV, productActulPriceTV;
        LinearLayout bottomLayout;
        ImageButton productlikeImage;
        ProgressBar productAdapterProgressBar;


        public ProductViewHolder(View itemView) {
            super(itemView);

            productImageview = (ImageView) itemView.findViewById(R.id.product_adapter_imageview);
            productNameTV = (TextView) itemView.findViewById(R.id.product_adapter_name_TV);
            productOfferPriceTV = (TextView) itemView.findViewById(R.id.product_adapter_offer_price_TV);
            productActulPriceTV = (TextView) itemView.findViewById(R.id.product_adapter_actual_price_TV);
            bottomLayout = (LinearLayout) itemView.findViewById(R.id.product_adapter_bottom_layout);
            productAdapterProgressBar = (ProgressBar) itemView.findViewById(R.id.product_progressbar);
            productlikeImage = (ImageButton) itemView.findViewById(R.id.likeImage);
            // productlikeImageRed= (ImageButton) itemView.findViewById(R.id.likeImageRed);

            productImageview.setOnClickListener(this);
            productlikeImage.setOnClickListener(this);
            // productlikeImageRed.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if (v == productImageview) {
                Log.d("Success", "MMM" + data.get(getAdapterPosition()).isCartCheck());
                Intent nextIntent = new Intent(mContext, ProductDescriptionActivity.class);
                nextIntent.putExtra(Const.PRODUCT_NAME, String.valueOf(data.get(getAdapterPosition()).getName()));
                nextIntent.putExtra(Const.PRODUCT_LIKES, String.valueOf(data.get(getAdapterPosition()).isCartCheck()));
                nextIntent.putExtra(Const.PRODUCT_ID_KEY, String.valueOf(data.get(getAdapterPosition()).getId()));
                mContext.startActivity(nextIntent);
            } else if (v == productlikeImage) {

                CartProduct cartProduct = new CartProduct();
                cartProduct.setId(String.valueOf(data.get(getAdapterPosition()).getId()));
                cartProduct.setName(data.get(getAdapterPosition()).getName());
                cartProduct.setImg_url(data.get(getAdapterPosition()).getImage().getSrc());
                cartProduct.setPrice(data.get(getAdapterPosition()).getVaraiants().get(0).getPrice());
                cartProduct.setQty(1);
                cartProduct.setTotal(String.valueOf(cartProduct.getQty() * Float.parseFloat(cartProduct.getPrice())));
                Log.d("Success", "Clicked Like image:::: " + data.get(getAdapterPosition()).isCartCheck());

                if (data.get(getAdapterPosition()).isCartCheck()) {
                    //Log.d("Success", "Clicked Like image");
                    sqLiteHelper.deleteCart(cartProduct);
                    data.get(getAdapterPosition()).setCartCheck(false);
                } else {
                    sqLiteHelper.addToCart(cartProduct);
                    data.get(getAdapterPosition()).setCartCheck(true);
                }
                notifyDataSetChanged();
            }
        }
    }


    static class LoadHolder extends RecyclerView.ViewHolder {
        public LoadHolder(View itemView) {
            super(itemView);
        }
    }

    static class NoDataHolder extends RecyclerView.ViewHolder {
        public NoDataHolder(View itemView) {
            super(itemView);
        }
    }
}
