import { initNotifications, showNotification } from "../utils/notificaciones.js";

initNotifications();

emailjs.init({
  publicKey: "1yAXMYL8iGTD1QQ42",
});

const contactForm = document.querySelector(".contact-form");

contactForm.addEventListener("submit", async (event) => {
  event.preventDefault();

  if (!validateForm()) {
    return;
  }

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

function validateName(name) {
  return name.trim().length >= 3;
}

function validatePhone(phone) {
  const phoneRegex = /^[0-9]{7,15}$/;

  return phoneRegex.test(phone);
}

function validateEmail(email) {
    const emailRegex =
        /^[a-zA-Z0-9._%+-]+@[a-zA-Z][a-zA-Z0-9-]*(\.[a-zA-Z0-9-]+)*\.[a-zA-Z]{2,}$/;

    return emailRegex.test(email.trim());
}

function validateService(service) {
  return service !== "";
}

function validateMessage(message) {
  return message.trim().length >= 10;
}

function validateForm() {
  const name = document.querySelector("#name").value;
  const phone = document.querySelector("#phone").value;
  const email = document.querySelector("#email").value;
  const service = document.querySelector("#service").value;
  const message = document.querySelector("#mensaje").value;

  if (!validateName(name)) {
    showNotification(
      "error",
      "Nombre inválido",
      "Ingresa un nombre válido."
    );

    return false;
  }

  if (!validatePhone(phone)) {
    showNotification(
      "error",
      "Celular inválido",
      "Ingresa un número de celular válido."
    );

    return false;
  }

  if (!validateEmail(email)) {
    showNotification(
      "error",
      "Email inválido",
      "Ingresa un correo electrónico válido."
    );

    return false;
  }

  if (!validateService(service)) {
    showNotification(
      "error",
      "Selecciona un servicio",
      "Selecciona el servicio que necesitas."
    );

    return false;
  }

  if (!validateMessage(message)) {
    showNotification(
      "error",
      "Mensaje demasiado corto",
      "Escribe un mensaje de al menos 10 caracteres."
    );

    return false;
  }

  return true;
}