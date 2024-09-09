package com.doctorappointment.initializer;

import com.doctorappointment.entites.Doctor;
import com.doctorappointment.entites.Patient;
import com.doctorappointment.repositories.DoctorRepository;
import com.doctorappointment.repositories.PatientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public DataInitializer(DoctorRepository doctorRepository, PatientRepository patientRepository) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public void run(String... args) {
        if (doctorRepository.count() == 0) {
            Doctor doctor1 = new Doctor("Dr. ali rahbar", "Cardiology");
            Doctor doctor2 = new Doctor("Dr. sima moradi", "Neurology");

            doctorRepository.save(doctor1);
            doctorRepository.save(doctor2);
        }

        if (patientRepository.count() == 0) {
            Patient patient1 = new Patient("saman dana", "0912558467");
            Patient patient2 = new Patient("ali radin", "661498755");

            patientRepository.save(patient1);
            patientRepository.save(patient2);
        }
    }
}