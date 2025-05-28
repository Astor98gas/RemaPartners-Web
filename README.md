# REMA Partners

REMA Partners es una plataforma integral de compraventa de maquinaria industrial de segunda mano, diseñada para facilitar las transacciones entre empresas del sector.
Disponible como página web y aplicación móvil, ofrece un espacio seguro y especializado donde las empresas pueden publicar, gestionar y encontrar maquinaria usada con filtros avanzados y características adaptadas a las necesidades industriales.

## Requerimientos funcionales:

**1.Base**

**1.1. Creación de Usuario**

**• RF1:** Los usuarios deben poder registrarse con correo electrónico y contraseña.

~~**• RF2:** Los usuarios deben poder registrarse utilizando su cuenta de Google.~~

**• RF3:** Validar que el correo electrónico sea único en el sistema.

**1.2. Perfiles de Usuario**

**• RF4:** Los usuarios deben poder personalizar sus perfiles con:

  • Foto de perfil.
  
  • Descripción personal.
  
  • Enlaces a redes sociales.
  
  • Historial de ventas y valoraciones.

**1.3. Seguimiento de Usuarios**

~~**• RF5:** Los usuarios deben poder seguir a vendedores favoritos para recibir notificaciones
sobre nuevos productos.~~

**1.4. Galería de Imágenes para Productos**

**• RF6:** Los usuarios deben poder subir múltiples imágenes por producto.

**• RF7:** Implementar un visor de imágenes con zoom y desplazamiento.

**2. Categorías**

**2.1. CRUD de Categorías**

**• RF8:** Los empleados deben poder crear, leer, actualizar y eliminar categorías.

**• RF9:** Cada categoría debe tener los siguientes campos mínimos:

  • Nombre (obligatorio).
  
  • Descripción (opcional).
  
**• RF10:** El sistema debe ser modular para permitir la adición de nuevos campos a las
categorías.

**2.2. Búsqueda Avanzada**

**• RF11:** Los usuarios deben poder buscar productos por:

  • Palabras clave.
  
  • Categorías.
  
  • Ubicación (radio de búsqueda).
  
  • Precio (rango mínimo y máximo).
  
  • Estado (nuevo, usado, en oferta).
  
**3. Productos**

**.1. Alta de Productos**
   
**• RF12:** Solo los usuarios con suscripción activa pueden publicar productos.

**• RF13:** Al crear un producto, el usuario debe seleccionar una categoría y cargar los campos
específicos asociados a esa categoría.

**• RF14:** El sistema debe permitir cargar la ubicación actual del producto.

**• RF15:** El usuario debe poder editar la ubicación del producto, seleccionando:

  • Ciudad.
  
**• RF16:** Campo descripción para poder indicar todo lo que necesiten.

**3.2. Historial de Productos**

**• RF17:** Mostrar fecha de actualización de producto.

**3.3. Productos Relacionados**

**• RF18:** Mostrar productos similares o relacionados en la página de un producto.

**4. Suscripciones**

**4.1. Suscripción para Publicidad**

**• RF19:** Implementar un sistema de suscripción utilizando Stripe para procesar pagos.

**• RF20:** Solo los usuarios con suscripción activa pueden publicar productos.

**• RF21:** Suscripción manual cada mes.

**4.2. Prueba Gratuita**

**• RF22:** Ofrecer una prueba gratuita de la suscripción para atraer nuevos usuarios.

**5. Notificaciones**

**5.1. Envío de Notificaciones**

**• RF23:** El back-end debe enviar notificaciones a los usuarios por:

  • Móvil.
  
**• RF24:** Los usuarios deben poder configurar qué tipos de notificaciones desean recibir en los
ajustes de su cuenta.

**6. Comentarios y Valoraciones**

**6.1. Respuestas a Comentarios**

**• RF25:** Permitir a los vendedores responder a los comentarios de los compradores.

**6.2. Valoraciones a Vendedores**

**• RF26:** Los usuarios deben poder valorar a los vendedores con una puntuación y un
comentario.

**• RF27:** Los vendedores deben poder validar los comentarios.

**• RF28:** Solo los administradores y trabajadores han de poder eliminar comentarios.

**7. Venta y Chat**

**7.1. Ofertas y Negociación**

**• RF29:** Permitir a los compradores hacer ofertas a los vendedores a través del chat.

**7.2. Chat entre Cliente y Vendedor**

**• RF30:** Implementar un sistema de chat en tiempo real entre el cliente y el vendedor.

**• RF31:** Los mensajes deben almacenarse en la base de datos para su consulta posterior.

**• RF32:** Los usuarios deben recibir notificaciones cuando reciban un nuevo mensaje en el
chat.

**8. Funcionalidades de Analítica y Reportes**

**8.1. Panel de Control para Vendedores**

**• RF33:** Proporcionar a los vendedores un panel de control con métricas como:

  • Número de vistas de sus productos.
  
  • Ventas realizadas.
  
  • Valoraciones promedio.
  
**9. Internacionalización**

**9.1. Idiomas**
    
**• RF34:** Ofrecer la aplicación en múltiples idiomas para llegar a un público global.

## Requerimientos no funcionales

**10.1. Seguridad**

**• RNF1:** Implementar autenticación segura (JWT, OAuth).

**• RNF2:** Asegurar que los datos sensibles (contraseñas, información de pago) estén
encriptados.

**10.2. Escalabilidad**

**• RNF3:** La aplicación debe ser escalable para soportar un crecimiento en el número de
usuarios y productos.

**10.3. Compatibilidad**

**• RNF4:** La aplicación móvil debe ser compatible con Android.

**• RNF5:** La aplicación web debe ser compatible con los principales navegadores (Chrome,
Firefox).

**11. Requerimientos Técnicos**

**11.1. Base de Datos**

**• RNF6:** Utilizar MongoDB para almacenar datos de categorías, productos, usuarios y
comentarios.

**11.2. Integración de APIs**

**• RNF7:** Integrar la API de Google para el registro con Google.

**• RNF8:** Integrar la API de Stripe para el procesamiento de pagos.

**11.3. Notificaciones**

**• RNF9:** Utilizar un servicio de notificaciones push (Firebase Cloud Messaging).

## Mejoras

- Se ha añadido la posibilidad de realizar una venta desde el chat.

- Se ha creado un apartado con las facturas de las ventas realizadas desde el chat

- En el panel de visualización de ventas los valores se convierten automáticamente a euros en tiempo real con el mercado.

- Las imágenes se cargan en baja calidad cuando no se requiere en la máxima calidad.

## Características

### Sin Rol

- **Home:** Muestra los productos que han subido los vendedores a la web. Con filtros para poder encontrar el producto que necesitas.

![Captura de pantalla_20250526_100324](https://github.com/user-attachments/assets/c67b67b6-676a-4b5e-bf87-12c60bebbc80)

  ### Android:

  ![Captura de pantalla_20250526_113854](https://github.com/user-attachments/assets/123bc716-143c-4a65-8ba1-d2a3ae08059f)

- **Login/Registro:** Para poder comprar o vender productos lo primero es registrarse o iniciar sesión

![Captura de pantalla_20250526_100757](https://github.com/user-attachments/assets/98ef5de8-7af9-4240-b213-0daedc2b84f0)

![Captura de pantalla_20250526_100806](https://github.com/user-attachments/assets/e6e97160-6f4d-4706-836b-1e295dfce586)

  ### Android:

  ![Captura de pantalla_20250526_113921](https://github.com/user-attachments/assets/095b5f5f-e327-43b7-8ae7-41bb64df0bef)

  ![Captura de pantalla_20250526_113927](https://github.com/user-attachments/assets/15a31582-bc3c-4230-9527-eb873ab4564a)

### Rol Comprador

- **Hacer Ofertas y Chat:** Una vez hayas iniciado sesión podrás hacer una oferta directamente desde la página del producto que quieres comprar. Y podrás mantener una conversación con el vendedor con tal de negociar las condiciones.

![Captura de pantalla_20250526_104615](https://github.com/user-attachments/assets/0b73302d-0b7e-4453-841a-a977083d97e5)

![Captura de pantalla_20250526_104643](https://github.com/user-attachments/assets/54f8f767-eb1f-4bf6-b9d8-86b1b50107c2)

![Captura de pantalla_20250526_104652](https://github.com/user-attachments/assets/8be9927a-5b52-4f51-8a03-580208976e28)

  ### Android:

  ![Captura de pantalla_20250526_114020](https://github.com/user-attachments/assets/ff14faf3-5b2e-4e2b-8732-57a0f1f9b0d7)

  ![Captura de pantalla_20250526_114026](https://github.com/user-attachments/assets/cc54df1a-e5a4-4370-b671-02e6761de2dc)
  
  ![Captura de pantalla_20250526_114033](https://github.com/user-attachments/assets/5381332d-21aa-486f-bc04-bd6cadfd20e3)

  ![Captura de pantalla_20250526_114041](https://github.com/user-attachments/assets/6e12c929-15c1-4e74-b839-a1c7a3e9cbf9)

- **Facturas:** En la app tendrás una sección para ver tus facturas de las compras que has realizado.

![Captura de pantalla_20250526_105739](https://github.com/user-attachments/assets/9dc42be3-83ca-48f6-8131-7253c7bea493)

![Captura de pantalla_20250526_105744](https://github.com/user-attachments/assets/f9b00abc-d1f0-46d7-8e82-22126ddcdd93)

- **Verificar Vendedores:** Desde la página del producto podrás ver la valoración del vendedor y sus redes sociales. Verificando así su profesionalidad. ¡También puedes valorarlos!!

![Captura de pantalla_20250526_110031](https://github.com/user-attachments/assets/824d51bf-bcaa-4ccc-a55e-778091d5413d)

![Captura de pantalla_20250526_110047](https://github.com/user-attachments/assets/7c48f095-822e-4c5d-b58c-3b173978e3f7)

![Captura de pantalla_20250526_110053](https://github.com/user-attachments/assets/d359327a-8684-4d8e-807f-462a2b426f96)

![Captura de pantalla_20250526_110109](https://github.com/user-attachments/assets/527d71b4-e6d2-4d79-9718-443719c190ac)

![Captura de pantalla_20250526_110125](https://github.com/user-attachments/assets/235fac42-6b9e-42e9-ae0d-b5554202e4dc)

- **Perfil y Opciones premium:** Si lo que buscas es crear tus propios anuncios, dirígete a tu perfil, rellénalo para tener más oportunidades de venta. Y suscríbete al servicio premium para obtener las ventajas.

![Captura de pantalla_20250526_110425](https://github.com/user-attachments/assets/c4da8afe-12db-4ff3-ae3b-c26a8a04fec1)

![Captura de pantalla_20250526_110431](https://github.com/user-attachments/assets/05befcc1-5f44-47d2-97fc-71ab228a9703)

![Captura de pantalla_20250526_110447](https://github.com/user-attachments/assets/5256db08-c89d-4fe3-a6a8-46fa9f92f8da)

![Captura de pantalla_20250526_110500](https://github.com/user-attachments/assets/1155d542-d3fd-4d77-bbb9-32c60e449d6d)

### Rol Vendedor

- **Subir tus productos:** ¡Ahora que tienes una suscripción puedes colgar tus propios productos!! Ya sea desde la página principal o desde tu perfil.

![Captura de pantalla_20250526_110953](https://github.com/user-attachments/assets/ea2fb2bd-24cd-4497-bba9-595a2210f2b6)

![Captura de pantalla_20250526_110959](https://github.com/user-attachments/assets/056e145b-5b7b-453f-bd16-b5a0737d936d)

![Captura de pantalla_20250526_111013](https://github.com/user-attachments/assets/feb1840e-b4d9-41f2-ad53-8be0f50abd82)

![Captura de pantalla_20250526_111317](https://github.com/user-attachments/assets/b7459cae-e651-484b-b439-b3d17ea18d49)

![Captura de pantalla_20250526_111323](https://github.com/user-attachments/assets/cae9ea5a-84c5-4247-8eec-606662ed619e)

- **Estadísticas de venta y visitas:** Desde tu perfil podrás acceder a tus estadísticas de visitas y ventas.

![Captura de pantalla_20250526_112715](https://github.com/user-attachments/assets/e7606d8e-baad-42f1-9069-568ee3ba8918)

![Captura de pantalla_20250526_112727](https://github.com/user-attachments/assets/2d174a84-f1cd-4e80-8295-37af4008ae34)

![Captura de pantalla_20250526_112735](https://github.com/user-attachments/assets/408c9482-c7c5-4c00-a8f5-c4163de77746)

![Captura de pantalla_20250526_112752](https://github.com/user-attachments/assets/bf274e33-9579-40a4-b7e7-f7cad0ec43f3)

![Captura de pantalla_20250526_112801](https://github.com/user-attachments/assets/736e79a0-64b4-4892-830c-009b173f84cb)

- **Feedback:** Accede a tu perfil público y responde a las valoraciones de tus compradores.

![Captura de pantalla_20250526_113257](https://github.com/user-attachments/assets/98840b77-bc8f-40de-acd8-8fc1bd8b050f)

![Captura de pantalla_20250526_113310](https://github.com/user-attachments/assets/5f0e1466-0653-4e93-8c33-1171476306fd)

![Captura de pantalla_20250526_113320](https://github.com/user-attachments/assets/a355043e-5907-49dd-8039-645584510015)

### Rol Trabajador / Administrador

- **Gestionar categorías:** Como trabajador podrás gestionar toda la aplicación, desde los productos a las valoraciones y las categorías.
Desde el panel lateral o el perfil puedes ir a la sección de las categorías, ya sea para editarlas, crear nuevas o eliminar alguna.

![Captura de pantalla_20250526_113550](https://github.com/user-attachments/assets/b6b72ed8-c1d6-44da-b1e3-2dcbb01f7785)

![Captura de pantalla_20250526_113606](https://github.com/user-attachments/assets/8523262c-aabc-4667-bf75-8e6fc2aee5a5)

## Arquitectura

![imagen](https://github.com/user-attachments/assets/67410167-5199-4e69-a485-a2b6ca6f36bb)

- **Back-End:**

![Captura de pantalla_20250526_114514](https://github.com/user-attachments/assets/9dc603ff-ac1f-4c1a-9f36-e75432c5f3cc)

- **Front-End:**

![Captura de pantalla_20250526_114548](https://github.com/user-attachments/assets/888c6f43-3a27-4d00-95c8-5668d0755849)


- **Android:**

![Captura de pantalla_20250526_114729](https://github.com/user-attachments/assets/e18ba111-10f5-4483-bfe0-4992840ccd55)

## Video

https://github.com/user-attachments/assets/5dc23fae-d5f2-4304-94bf-2526695347ed

