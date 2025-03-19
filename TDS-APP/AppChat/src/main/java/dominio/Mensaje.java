package dominio;


import java.time.LocalDateTime;



public class Mensaje implements Comparable<Mensaje> {
	
	public static final int ENVIADO = 0;
	public static final int RECIBIDO = 1;
	
	
	private String texto;
	private int codigo;
	private  LocalDateTime fechaYhora;	
	private int emoticono;
	private int tipo; 
	private Usuario emisor; 
	private Contacto receptor;
	
	// Constructor.
		public Mensaje(String texto, Usuario emisor, Contacto receptor, int tipo) {
			this.texto = texto;
			this.fechaYhora = LocalDateTime.now();
			this.emisor = emisor;
			this.receptor = receptor;
			this.tipo = tipo;
		}

		public Mensaje(int emoticono, Usuario emisor, Contacto receptor, int tipo) {
			this.texto = "";
			this.fechaYhora = LocalDateTime.now();
			this.emoticono = emoticono;
			this.emisor = emisor;
			this.receptor = receptor;
			this.tipo = tipo;
		}

		public Mensaje(String texto, int emoticono, Usuario emisor, Contacto receptor, int tipo) {
			this.texto = texto;
			this.emoticono = emoticono;
			this.fechaYhora= LocalDateTime.now();
			this.tipo = tipo;
			this.emisor = emisor;
			this.receptor = receptor;
		}


	
	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public Mensaje() {
	}
	
	public int getEmoticono() {
		return emoticono;
	}
	
	public void setEmoticono(int emoticono) {
		 this.emoticono = emoticono;
	}

	public String getTexto() {
		return texto;
	}
	
	public void setTexto(String texto) {
		this.texto = texto;
	}

	public int getCodigo() {
		return codigo;
	}
	
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	
	public Usuario getEmisor() {
		return emisor;
	}
	
	public void setEmisor(Usuario emisor) {
		this.emisor = emisor;
	}
	
	public Contacto getReceptor() {
		return receptor;
	}
	
	public void setReceptor(Contacto receptor){
		this.receptor = receptor;
	}
	
	public LocalDateTime getFechaYhora() {
		return fechaYhora;
	}	
	
	public void setFechaYhora(LocalDateTime fechaYhora) {
		this.fechaYhora = fechaYhora;
	}
	
	@Override
	public int compareTo(Mensaje m) {
		return fechaYhora.compareTo(m.getFechaYhora());
	}

	
	
}
