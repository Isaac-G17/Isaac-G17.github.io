import { initNotifications, showNotification } from "../utils/notificaciones.js";

initNotifications();

emailjs.init({
  publicKey: "1yAXMYL8iGTD1QQ42",
});

const contactForm = document.querySelector(".contact-form");

contactForm.addEventListener("submit", async (event) => {
  event.preventDefault();

  try {
    const response = await emailjs.sendForm(
      "service_z34lvvl",
      "template_2lbrv1c",
      contactForm,
    );

    console.log("Mensaje enviado:", response.status, response.text);

    showNotification(
            'success',
            '¡Mensaje enviado!',
            'Gracias por contactarme. Me pondré en contacto contigo pronto.'
        );

    contactForm.reset();
  } catch (error) {
    console.error("Error al enviar el mensaje:", error);

    showNotification(
            'error',
            'No se pudo enviar',
            'Ocurrió un problema. Inténtalo nuevamente.'
        );
  }
});
