package com.alayyubi06.islamiapp;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListProdukAdapter extends RecyclerView.Adapter<ListProdukAdapter.ProdukHolder> {
    private ArrayList<Produk> listProduk;

    public ListProdukAdapter(ArrayList<Produk> listProduk) {
    this.listProduk = listProduk;
    }

    @Override
    public ProdukHolder onCreateViewHolder(ViewGroup parent, int position) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    return new ProdukHolder(inflater.inflate(R.layout.item_layout, parent, false));
    }
    @Override
    public void onBindViewHolder(ProdukHolder produkHolder, int position) {
    produkHolder.bind(listProduk.get(position));
    }
    @Override
    public int getItemCount() {
    return listProduk.size();
    }
    public class ProdukHolder extends RecyclerView.ViewHolder {
        private TextView mProduk;
        private TextView mSertifikat;
        private TextView mProdusen;
        private TextView mBerlaku;
        public ProdukHolder(View itemView) {
            super(itemView);
            mProduk = (TextView)itemView.findViewById(R.id.tv_nama);
            mSertifikat = (TextView)itemView.findViewById(R.id.tv_no_sertifikat);
            mProdusen = (TextView)itemView.findViewById(R.id.tv_produsen);
            mBerlaku = (TextView)itemView.findViewById(R.id.tv_berlaku);
        }
        public void bind(Produk produk) {
            mProduk.setText(produk.nama);
            mSertifikat.setText(produk.no_sertifikat);
            mProdusen.setText(produk.produsen);
            mBerlaku.setText(produk.berlaku);
        }
    }
}

