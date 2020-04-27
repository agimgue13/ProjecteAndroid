package com.example.authme.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ProgressBar;

public class Producto implements Parcelable, Comparable {
    String id;
    String imagen;
    String name;
    String descripcion;
    String tipo;
    String marca;
    Float precio;

    public Producto() {
    }

    public Producto(String id, String imagen, String name, String descripcion, String tipo, String marca, Float precio) {
        this.id = id;
        this.imagen = imagen;
        this.name = name;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.marca = marca;
        this.precio = precio;
    }

    protected Producto(Parcel in) {
        id = in.readString();
        imagen = in.readString();
        name = in.readString();
        descripcion = in.readString();
        tipo = in.readString();
        marca = in.readString();
        if (in.readByte() == 0) {
            precio = null;
        } else {
            precio = in.readFloat();
        }
    }

    public static final Creator<Producto> CREATOR = new Creator<Producto>() {
        @Override
        public Producto createFromParcel(Parcel in) {
            return new Producto(in);
        }

        @Override
        public Producto[] newArray(int size) {
            return new Producto[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(id);
        parcel.writeString(imagen);
        parcel.writeString(name);
        parcel.writeString(descripcion);
        parcel.writeString(tipo);
        parcel.writeString(marca);
        if (precio == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(precio);
        }
    }

    @Override
    public int compareTo(Object o) {
        Producto p = (Producto) o;
        return this.precio.compareTo(p.precio);
    }
}
