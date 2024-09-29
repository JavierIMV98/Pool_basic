package cl.javier.pool.adaptadores;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cl.javier.pool.R;
import cl.javier.pool.VerActivity;
import cl.javier.pool.entidades.Mesas;

public class ListaMesasAdapter extends RecyclerView.Adapter<ListaMesasAdapter.MesaViewHolder> {
    ArrayList<Mesas> listaMesas;
    public ListaMesasAdapter(ArrayList<Mesas> listaMesas){
        this.listaMesas = listaMesas;
    }

    @NonNull
    @Override
    public ListaMesasAdapter.MesaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_item_mesa,null,false);
        return new MesaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaMesasAdapter.MesaViewHolder holder, int position) {
        switch (listaMesas.get(position).getNro()){
            case 1:
                holder.iv_nro.setImageResource(R.drawable.poolball1);
                break;
            case 2:
                holder.iv_nro.setImageResource(R.drawable.poolball2);
                break;
            case 3:
                holder.iv_nro.setImageResource(R.drawable.poolball3);
                break;
            case 4:
                holder.iv_nro.setImageResource(R.drawable.poolball4);
                break;
            case 5:
                holder.iv_nro.setImageResource(R.drawable.poolball5);
                break;
            case 6:
                holder.iv_nro.setImageResource(R.drawable.poolball6);
                break;
            case 7:
                holder.iv_nro.setImageResource(R.drawable.poolball7);
                break;
            case 8:
                holder.iv_nro.setImageResource(R.drawable.poolball8);
                break;
            case 9:
                holder.iv_nro.setImageResource(R.drawable.poolball9);
                break;
            case 10:
                holder.iv_nro.setImageResource(R.drawable.poolball10);
                break;
            case 11:
                holder.iv_nro.setImageResource(R.drawable.poolball11);
                break;
            case 12:
                holder.iv_nro.setImageResource(R.drawable.poolball12);
                break;
            case 0:
                holder.iv_nro.setImageResource(R.drawable.poolball9);
                break;
            default:
                holder.iv_nro.setImageResource(R.drawable.poolball9);
        }
        holder.tvprecio.setText(String.valueOf(listaMesas.get(position).getPrecio()));
        holder.tvhora.setText(listaMesas.get(position).getHora_inicio());

    }

    @Override
    public int getItemCount() {
        return listaMesas.size();
    }

    public class MesaViewHolder extends RecyclerView.ViewHolder {
        TextView tvprecio,tvhora;
        ImageView iv_nro;
        public MesaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvhora = itemView.findViewById(R.id.tvhora);
            tvprecio = itemView.findViewById(R.id.tvprecio);
            iv_nro = itemView.findViewById(R.id.ivnro);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent i = new Intent(context, VerActivity.class);
                    i.putExtra("nro",listaMesas.get(getAdapterPosition()).getNro());
                    context.startActivity(i);

                }
            });
        }
    }
}
