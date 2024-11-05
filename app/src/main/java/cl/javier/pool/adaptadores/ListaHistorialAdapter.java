package cl.javier.pool.adaptadores;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cl.javier.pool.R;
import cl.javier.pool.VerActivity;
import cl.javier.pool.db.DbHistorialMesa;
import cl.javier.pool.entidades.HistorialMesa;
import cl.javier.pool.entidades.Mesas;

public class ListaHistorialAdapter extends RecyclerView.Adapter<ListaHistorialAdapter.HistorialViewHolder> {
    ArrayList<HistorialMesa> listaMesas;
    Context context;
    public ListaHistorialAdapter(Context context,ArrayList<HistorialMesa> listaMesas){
        this.listaMesas = listaMesas;
        this.context = context;

    }

    @NonNull
    @Override
    public ListaHistorialAdapter.HistorialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_item_historial_mesa,null,false);
        return new ListaHistorialAdapter.HistorialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaHistorialAdapter.HistorialViewHolder holder, int position) {
        HistorialMesa mesa = listaMesas.get(position);
        holder.tvnro.setText(String.valueOf(listaMesas.get(position).getNro()));

        holder.tvhora.setText(listaMesas.get(position).getHora_inicio());
        holder.tvhoraf.setText(listaMesas.get(position).getHora_final());
        holder.tvval.setText(String.valueOf(listaMesas.get(position).getValor()));

    }

    @Override
    public int getItemCount() {
        return listaMesas.size();
    }

    public class HistorialViewHolder extends RecyclerView.ViewHolder {
        TextView tvnro,tvhora,tvhoraf,tvval;
        public HistorialViewHolder(@NonNull View itemView) {
            super(itemView);
            tvnro = itemView.findViewById(R.id.tvnroh);
            tvhora = itemView.findViewById(R.id.tvhorah);
            tvhoraf = itemView.findViewById(R.id.tvhorafh);
            tvval = itemView.findViewById(R.id.tvvalh);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mostrarDialogoEliminar(position);
                    }
                    return true;
                }
            });
        }
    }
    private void mostrarDialogoEliminar(int position) {
        HistorialMesa mesa = listaMesas.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirmar eliminación");
        builder.setMessage("¿Deseas eliminar esta mesa del historial?");
        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DbHistorialMesa dbHistorialMesa = new DbHistorialMesa(context);
                if (dbHistorialMesa.eliminarMesaHistorial(mesa.getId())) {
                    listaMesas.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, listaMesas.size());
                }
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
