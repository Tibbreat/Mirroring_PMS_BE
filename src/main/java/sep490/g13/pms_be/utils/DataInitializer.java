package sep490.g13.pms_be.utils;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import sep490.g13.pms_be.entities.*;
import sep490.g13.pms_be.model.request.classes.AddClassRequest;
import sep490.g13.pms_be.model.request.route.AddRouteRequest;
import sep490.g13.pms_be.model.request.route.AddVehicleIntoRouteRequest;
import sep490.g13.pms_be.repository.*;
import sep490.g13.pms_be.service.entity.ClassService;
import sep490.g13.pms_be.service.entity.RouteService;
import sep490.g13.pms_be.service.entity.VehicleService;
import sep490.g13.pms_be.utils.enums.RoleEnums;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class DataInitializer {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FoodServiceProviderRepo foodServiceProviderRepo;

    @Autowired
    private TransportServiceProviderRepo transportServiceProviderRepo;

    @Autowired
    private SchoolRepo schoolRepo;
    @Autowired
    private VehicleRepo vehicleRepo;
    @Autowired
    private VehicleImageRepo vehicleImageRepo;

    @Autowired
    private RouteRepo routeRepo;

    @Autowired
    private ClassRepo classRepo;

    @Autowired
    private ClassService classService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private SchoolYearInformationRepo schoolYearInformationRepo;
    @Autowired
    private AdmissionFileRepo admissionFileRepo;
    @Autowired
    private VehicleService vehicleService;

    @PostConstruct
    public void init() throws ParseException {
        initializeSchool();
    }

    private void initializeSchool() throws ParseException {
        if (schoolRepo.count() == 0) {
            School school = School.builder().schoolName("Trường mầm non Đông Phương Yên").schoolAddress("Đồi 3, Đông Phương Yên, Chương Mỹ, Huyện Chương Mỹ, Hà Nội").phoneContact("0978 056 529").emailContact("mnthachhoa@edu.vn").build();
            schoolRepo.save(school);
            if (userRepo.count() == 0) {
                User admin = userRepo.save(User.builder().role(RoleEnums.ADMIN).isActive(true).email("admin.pms@gmail.com").fullName("Nguyễn Trung Kiên").phone("0943494158").username("admin").password(passwordEncoder.encode("admin")).build());
                school.setPrincipal(userRepo.findByUsername("admin"));
                schoolRepo.save(school);

                //2024-2025 information
                if (schoolYearInformationRepo.count() == 0) {
                    SchoolYearInformation schoolYearInformation_24_25 = SchoolYearInformation.builder().school(school).academicYear("2024-2025").openingDay("2024-09-05").closingDay("2025-05-20").totalStudentLevel1(20).totalStudentLevel2(25).totalStudentLevel3(30).totalClassLevel1(3).totalClassLevel2(2).totalClassLevel3(3).totalEnrolledStudents(200).onlineEnrollmentStartDate(LocalDate.parse("2024-07-01")).onlineEnrollmentEndDate(LocalDate.parse("2024-08-15")).offlineEnrollmentStartDate(LocalDate.parse("2024-08-16")).offlineEnrollmentEndDate(LocalDate.parse("2024-08-23")).build();
                    SchoolYearInformation schoolYearInformation_23_24 = SchoolYearInformation.builder().school(school).academicYear("2023-2024").openingDay("2023-09-05").closingDay("2024-05-20").totalStudentLevel1(20).totalStudentLevel2(25).totalStudentLevel3(30).totalClassLevel1(1).totalClassLevel2(2).totalClassLevel3(3).totalEnrolledStudents(185).onlineEnrollmentStartDate(LocalDate.parse("2023-07-01")).onlineEnrollmentEndDate(LocalDate.parse("2023-08-15")).offlineEnrollmentStartDate(LocalDate.parse("2023-08-16")).offlineEnrollmentEndDate(LocalDate.parse("2023-08-23")).build();
                    SchoolYearInformation savedData_24_25 = schoolYearInformationRepo.save(schoolYearInformation_24_25);
                    SchoolYearInformation savedData_23_24 = schoolYearInformationRepo.save(schoolYearInformation_23_24);
                    if (admissionFileRepo.count() == 0) {
                        AdmissionFileInitData(savedData_24_25);
                        AdmissionFileInitData(savedData_23_24);


                    }
                }
                //Teacher
                User teacher_1 = userRepo.save(User.builder().role(RoleEnums.TEACHER).isActive(true).email("TrinhHK.pms@gmail.com").phone("0943494158").username("TrinhHK").fullName("Hoàng Kiều Trinh").imageLink("https://res.cloudinary.com/dhb38seql/image/upload/v1730001415/ucwkxqoqaw0qyd0bhozb.jpg").idCardNumber("941654846484").address("78 Đường Trường Chinh, Quận Thanh Xuân, Hà Nội").contractType("Hợp đồng lao động có thời hạn 6 tháng").password(passwordEncoder.encode("123456")).build());
                User teacher_2 = userRepo.save(User.builder().role(RoleEnums.TEACHER).isActive(true).email("AnhNP.pms@gmail.com").phone("0912345678").username("AnhNP").fullName("Nguyễn Phương Anh").imageLink("https://res.cloudinary.com/dhb38seql/image/upload/v1730012932/tmnis5edzqmn1lwcjh9v.jpg").idCardNumber("321654987123").address("78 Đường Trường Chinh, Quận Thanh Xuân, Hà Nội").contractType("Hợp đồng lao động không xác định thời hạn").password(passwordEncoder.encode("123456")).build());
                User teacher_3 = userRepo.save(User.builder().role(RoleEnums.TEACHER).isActive(true).email("LyLC.pms@gmail.com").phone("0987558512").username("LyLC").fullName("Lê Cẩm Ly").imageLink("https://res.cloudinary.com/dhb38seql/image/upload/v1730013010/zrkh22ud0z3bgoaurfid.jpg").idCardNumber("987654321012").address("604 đường Bà Triệu, Thị Trấn Hậu Lộc, huyện Hậu Lộc, tỉnh Thanh Hóa").contractType("Hợp đồng lao động có thời hạn 1 năm").password(passwordEncoder.encode("123456")).build());
                User teacher_4 = userRepo.save(User.builder().role(RoleEnums.TEACHER).isActive(true).email("ChiNT.pms@gmail.com").phone("0968711752").username("ChiNT").fullName("Nguyễn Thùy Chi").imageLink("https://res.cloudinary.com/dhb38seql/image/upload/v1730013082/onrehbphd1stpicqzurd.jpg").idCardNumber("321654987123").address("56 Phố Huế, Quận Hai Bà Trưng, Hà Nội").contractType("Hợp đồng lao động có thời hạn 1 năm").password(passwordEncoder.encode("123456")).build());
                User teacher_5 = userRepo.save(User.builder().role(RoleEnums.TEACHER).isActive(true).email("MaiTN.pms@gmail.com").phone("0987658632").username("MaiTN").fullName("Trần Ngọc Mai").imageLink("https://res.cloudinary.com/dhb38seql/image/upload/v1730013202/tcrcxtk0tq122vxqpvfk.jpg").idCardNumber("445566778899").address("123 Đường Thanh Niên, Ba Đình, Hà Nội").contractType("Hợp đồng lao động không xác định thời hạn").password(passwordEncoder.encode("123456")).build());
                User teacher_6 = userRepo.save(User.builder().role(RoleEnums.TEACHER).isActive(true).email("ThanhPT.pms@gmail.com").phone("0973344556").username("ThanhPT").fullName("Phạm Thị Thanh").idCardNumber("112233445566").address("90 Đường Giải Phóng, Quận Hai Bà Trưng, Hà Nội").contractType("Hợp đồng lao động có thời hạn 2 năm").password(passwordEncoder.encode("123456")).build());
                User teacher_7 = userRepo.save(User.builder().role(RoleEnums.TEACHER).isActive(true).email("HoaLT.pms@gmail.com").phone("0911223344").username("HoaLT").fullName("Lê Thị Hoa").idCardNumber("556677889900").address("15 Đường Kim Mã, Quận Ba Đình, Hà Nội").contractType("Hợp đồng lao động không xác định thời hạn").password(passwordEncoder.encode("123456")).build());
                User teacher_8 = userRepo.save(User.builder().role(RoleEnums.TEACHER).isActive(true).email("BinhPT.pms@gmail.com").phone("0909988776").username("BinhPT").fullName("Phan Thị Bình").idCardNumber("998877665544").address("72 Đường Tây Sơn, Quận Đống Đa, Hà Nội").contractType("Hợp đồng lao động có thời hạn 6 tháng").password(passwordEncoder.encode("123456")).build());
                User teacher_9 = userRepo.save(User.builder().role(RoleEnums.TEACHER).isActive(true).email("MinhTD.pms@gmail.com").phone("0933445566").username("MinhTD").fullName("Trần Thị Minh").idCardNumber("334455667788").address("47 Đường Nguyễn Lương Bằng, Quận Đống Đa, Hà Nội").contractType("Hợp đồng lao động không xác định thời hạn").password(passwordEncoder.encode("123456")).build());
                User teacher_10 = userRepo.save(User.builder().role(RoleEnums.TEACHER).isActive(true).email("LanPT.pms@gmail.com").phone("0945566778").username("LanPT").fullName("Phan Thị Lan").idCardNumber("776655443322").address("32 Đường Bạch Mai, Quận Hai Bà Trưng, Hà Nội").contractType("Hợp đồng lao động có thời hạn 1 năm").password(passwordEncoder.encode("123456")).build());

                //Class manager
                User classManager_1 = userRepo.save(User.builder().role(RoleEnums.CLASS_MANAGER).isActive(true).email("LienLT.pms@gmail.com").phone("0966658732").username("LienLT").fullName("Lê Thị Liên").imageLink("https://res.cloudinary.com/dhb38seql/image/upload/v1730013615/yotjwpxn3b8oj9gnolkk.jpg").idCardNumber("445566778899").address("123 Đường Thanh Niên, Ba Đình, Hà Nội").password(passwordEncoder.encode("123456")).build());
                User classManager_2 = userRepo.save(User.builder().role(RoleEnums.CLASS_MANAGER).isActive(true).email("TuNH.pms@gmail.com").phone("0987458112").username("TuNH").fullName("Nguyễn Hồng Tú").imageLink("https://res.cloudinary.com/dhb38seql/image/upload/v1730013588/qoshpp9leu55mxap9dvk.jpg").idCardNumber("038702000762").address("123 Đường Thanh Niên, Ba Đình, Hà Nội").password(passwordEncoder.encode("123456")).build());
                User classManager_3 = userRepo.save(User.builder().role(RoleEnums.CLASS_MANAGER).isActive(true).email("TraBT.pms@gmail.com").phone("0984656632").username("TraBT").fullName("Bùi Thu Trà").imageLink("https://res.cloudinary.com/dhb38seql/image/upload/v1730013503/xlyshmj3lrgrhuubnrqw.jpg").idCardNumber("038702000762").address("123 Đường Thanh Niên, Ba Đình, Hà Nội").password(passwordEncoder.encode("123456")).build());

                //Kitchen manager
                userRepo.save(User.builder().role(RoleEnums.KITCHEN_MANAGER).isActive(true).email("SonLT.pms@gmail.com").phone("0984096632").username("SonLT").fullName("Lê Trường Sơn").imageLink("https://res.cloudinary.com/dhb38seql/image/upload/v1730013305/l3s7hx3p8ezk9omrscuk.jpg").idCardNumber("038702010762").address("123 Đường Thanh Niên, Ba Đình, Hà Nội").password(passwordEncoder.encode("123456")).build());

                //Transport manager
                userRepo.saveAll(Arrays.asList(
                        User.builder().role(RoleEnums.TRANSPORT_MANAGER).isActive(true).email("DuyDD.pms@gmail.com").phone("0984096632").username("DuyDD").fullName("Duy Đặng Đức").idCardNumber("038702010762").address("123 Đường Thanh Niên, Ba Đình, Hà Nội").password(passwordEncoder.encode("123456")).build(),
                        User.builder().role(RoleEnums.TRANSPORT_MANAGER).isActive(true).email("LinhNT.pms@gmail.com").phone("0984096633").username("LinhNT").fullName("Nguyễn Thị Linh").idCardNumber("038702010763").address("456 Đường Cầu Giấy, Cầu Giấy, Hà Nội").password(passwordEncoder.encode("123456")).build(),
                        User.builder().role(RoleEnums.TRANSPORT_MANAGER).isActive(true).email("HieuTT.pms@gmail.com").phone("0984096634").username("HieuTT").fullName("Trần Tiến Hiếu").idCardNumber("038702010764").address("789 Đường Kim Mã, Ba Đình, Hà Nội").password(passwordEncoder.encode("123456")).build(),
                        User.builder().role(RoleEnums.TRANSPORT_MANAGER).isActive(true).email("MaiLT.pms@gmail.com").phone("0984096635").username("MaiLT").fullName("Lê Thị Mai").idCardNumber("038702010765").address("101 Đường Hoàng Hoa Thám, Ba Đình, Hà Nội").password(passwordEncoder.encode("123456")).build(),
                        User.builder().role(RoleEnums.TRANSPORT_MANAGER).isActive(true).email("QuanNV.pms@gmail.com").phone("0984096636").username("QuanNV").fullName("Nguyễn Văn Quân").idCardNumber("038702010766").address("202 Đường Láng Hạ, Đống Đa, Hà Nội").password(passwordEncoder.encode("123456")).build()));


                //Data for class
                if (classRepo.count() == 0) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date defaultOpeningDay = dateFormat.parse("2024-09-05");
                    Date pastOpeningDay = dateFormat.parse("2023-09-05");

                    classService.createNewClass(AddClassRequest.builder().className("Lớp Mầm Non 1A").ageRange("3-4").openingDay(defaultOpeningDay).teacherId(teacher_1.getId()).managerId(classManager_1.getId()).createdBy(admin.getId()).academicYear("2024-2025").build());
                    classService.createNewClass(AddClassRequest.builder().className("Lớp Mầm Non 2A").ageRange("3-4").openingDay(defaultOpeningDay).teacherId(teacher_2.getId()).managerId(classManager_1.getId()).createdBy(admin.getId()).academicYear("2024-2025").build());
                    classService.createNewClass(AddClassRequest.builder().className("Lớp Chồi 1A").ageRange("4-5").openingDay(defaultOpeningDay).teacherId(teacher_3.getId()).managerId(classManager_2.getId()).createdBy(admin.getId()).academicYear("2024-2025").build());
                    classService.createNewClass(AddClassRequest.builder().className("Lớp Chồi 2A").ageRange("4-5").openingDay(defaultOpeningDay).teacherId(teacher_4.getId()).managerId(classManager_2.getId()).createdBy(admin.getId()).academicYear("2024-2025").build());
                    classService.createNewClass(AddClassRequest.builder().className("Lớp Lá 1A").ageRange("5-6").openingDay(defaultOpeningDay).teacherId(teacher_5.getId()).managerId(classManager_3.getId()).createdBy(admin.getId()).academicYear("2024-2025").build());

                    classService.createClassInThePast(AddClassRequest.builder().className("Lớp Mầm Non 1B").ageRange("3-4").openingDay(pastOpeningDay).teacherId(teacher_1.getId()).managerId(classManager_1.getId()).createdBy(admin.getId()).academicYear("2023-2024").build());
                    classService.createClassInThePast(AddClassRequest.builder().className("Lớp Mầm Non 2B").ageRange("3-4").openingDay(pastOpeningDay).teacherId(teacher_3.getId()).managerId(classManager_1.getId()).createdBy(admin.getId()).academicYear("2023-2024").build());
                    classService.createClassInThePast(AddClassRequest.builder().className("Lớp Chồi 1B").ageRange("4-5").openingDay(pastOpeningDay).teacherId(teacher_5.getId()).managerId(classManager_2.getId()).createdBy(admin.getId()).academicYear("2023-2024").build());
                    classService.createClassInThePast(AddClassRequest.builder().className("Lớp Chồi 2B").ageRange("4-5").openingDay(pastOpeningDay).teacherId(teacher_7.getId()).managerId(classManager_2.getId()).createdBy(admin.getId()).academicYear("2023-2024").build());
                    classService.createClassInThePast(AddClassRequest.builder().className("Lớp Lá 1B").ageRange("5-6").openingDay(pastOpeningDay).teacherId(teacher_8.getId()).managerId(classManager_3.getId()).createdBy(admin.getId()).academicYear("2023-2024").build());

                }

                if (foodServiceProviderRepo.count() == 0) {
                    foodServiceProviderRepo.save(FoodServiceProvider.builder().providerName("Công ty TNHH Thực phẩm Hà Nội").providerTaxCode("0108765432").providerPhone("02434567890").providerEmail("contact@thucphamhn.com").providerAddress("Số 20, Phố Láng Hạ, Quận Ba Đình, Hà Nội").representativeName("Nguyễn Thị B").representativePosition("Giám đốc").bankName("VCB").isActive(true).beneficiaryName("Nguyễn Thị B").bankAccountNumber("00234567890").school(school).build());
                }
                if (transportServiceProviderRepo.count() == 0) {
                    TransportServiceProvider provider = transportServiceProviderRepo.save(TransportServiceProvider.builder().providerName("Công ty TNHH Dịch vụ Đưa đón Hà Nội").providerTaxCode("0108765432").providerPhone("02434567890").providerEmail("contact@dichvuduadonhn.com").providerAddress("Số 20, Phố Láng Hạ, Quận Ba Đình, Hà Nội").representativeName("Nguyễn Văn C").representativePosition("Giám đốc").bankName("VCB").beneficiaryName("Nguyễn Văn C").bankAccountNumber("00234567890").isActive(true).totalVehicle(10).school(school).build());

                    if (vehicleRepo.count() == 0) {

                        // Xe 1 data and images
                        List<String> image_1 = Arrays.asList("https://res.cloudinary.com/dhb38seql/image/upload/v1729829779/fz4qysuaxpn0mxc28bde.jpg", "https://res.cloudinary.com/dhb38seql/image/upload/v1729829630/cgkwaqy80zwk14igrgwo.jpg", "https://res.cloudinary.com/dhb38seql/image/upload/v1729829315/fbpmpd0ecuz9utmqhfc1.jpg", "https://res.cloudinary.com/dhb38seql/image/upload/v1729829313/txqvcyiv38hwoy6uxvkp.jpg", "https://res.cloudinary.com/dhb38seql/image/upload/v1729829311/mkevqi6dxr1j6tyunu88.jpg");

                        Vehicle vehicle_1 = vehicleRepo.save(Vehicle.builder().vehicleName("Xe 1").manufacturer("Hyundai").numberOfSeats(29).color("Be").licensePlate("30A-67890").driverName("Nguyễn Văn A").driverPhone("0971234567").transport(provider).numberChildrenRegistered(0).isActive(true).build());

                        image_1.forEach(url -> vehicleImageRepo.save(VehicleImage.builder().vehicle(vehicle_1).imageUrl(url).build()));

                        // Xe 2 data and images
                        List<String> image_2 = Arrays.asList("https://res.cloudinary.com/dhb38seql/image/upload/v1729823533/doaaqbgs7itekk5vyjkv.jpg", "https://res.cloudinary.com/dhb38seql/image/upload/v1729823531/jgtxjsgr5wlqekfcl24o.jpg", "https://res.cloudinary.com/dhb38seql/image/upload/v1729823530/vg02eoj1bgehkay9elwa.jpg", "https://res.cloudinary.com/dhb38seql/image/upload/v1729823529/zi7xj6gzf41xe4y6berd.jpg", "https://res.cloudinary.com/dhb38seql/image/upload/v1729823527/w33bspih0dvr7cigchaq.jpg");

                        Vehicle vehicle_2 = vehicleRepo.save(Vehicle.builder().vehicleName("Xe 2").manufacturer("Hyundai").numberOfSeats(29).color("Xanh trắng").licensePlate("29B-51935").driverName("Trần Văn B").driverPhone("0962345678").transport(provider).numberChildrenRegistered(0).isActive(true).build());

                        image_2.forEach(url -> vehicleImageRepo.save(VehicleImage.builder().vehicle(vehicle_2).imageUrl(url).build()));

                        List<String> image_3 = Arrays.asList("https://res.cloudinary.com/dhb38seql/image/upload/v1730290226/l_1729668245.582_uki7bl.jpg", "https://res.cloudinary.com/dhb38seql/image/upload/v1730290226/l_1729668246.402_fnzeum.jpg", "https://res.cloudinary.com/dhb38seql/image/upload/v1730290226/l_1729668247.881_lnpnaq.jpg", "https://res.cloudinary.com/dhb38seql/image/upload/v1730290226/l_1729668248.305_iyhktz.jpg");

                        Vehicle vehicle_3 = vehicleRepo.save(Vehicle.builder().vehicleName("Xe 3").manufacturer("Hyundai").numberOfSeats(29).color("Trắng").licensePlate("29B-12345").driverName("Trần Văn B").driverPhone("0962345678").transport(provider).numberChildrenRegistered(0).isActive(true).build());

                        image_3.forEach(url -> vehicleImageRepo.save(VehicleImage.builder().vehicle(vehicle_3).imageUrl(url).build()));

                        List<String> image_4 = Arrays.asList("https://res.cloudinary.com/dhb38seql/image/upload/v1730290224/l_1729464758.151_j6dd8g.jpg", "https://res.cloudinary.com/dhb38seql/image/upload/v1730290225/l_1729464760.668_jwumih.jpg", "https://res.cloudinary.com/dhb38seql/image/upload/v1730290225/l_1729464760.302_mehrt1.jpg", "https://res.cloudinary.com/dhb38seql/image/upload/v1730290225/l_1729464759.618_qveneh.jpg", "https://res.cloudinary.com/dhb38seql/image/upload/v1730290226/l_1729464761.461_yetrng.jpg");

                        Vehicle vehicle_4 = vehicleRepo.save(Vehicle.builder().vehicleName("Xe 4").manufacturer("Isuzu").numberOfSeats(34).color("Xanh lá cây").licensePlate("29D-26554").driverName("Driver A").driverPhone("0962345678").transport(provider).numberChildrenRegistered(0).isActive(true).build());

                        image_4.forEach(url -> vehicleImageRepo.save(VehicleImage.builder().vehicle(vehicle_4).imageUrl(url).build()));
                    }
                }
                if (routeRepo.count() == 0) {
                    Route route_1 = routeService.createRoute(AddRouteRequest.builder().routeName("Tuyến 1: UBND xã Phú Kim - Trường Mầm Non Thạch Hòa").startLocation("UBND xã Phú Kim").endLocation("Trường Mầm Non Thạch Hòa").pickupTime("6:30").dropOffTime("17:15").createdBy(admin.getId()).stopLocations(Arrays.asList("UBND xã Phú Kim", "Chợ Hòa Lạc", "Đình Quan Cần", "Trường Tiểu học Cần Kiệm")).build());
                    Route route_2 = routeService.createRoute(AddRouteRequest.builder().routeName("Tuyến 2: UBND xã Bình Yên - Trường Mầm Non Thạch Hòa").startLocation("UBND xã Bình Yên").endLocation("Trường Mầm Non Thạch Hòa").pickupTime("6:15").dropOffTime("17:00").createdBy(admin.getId()).stopLocations(Arrays.asList("UBND xã Bình Yên", "Chợ Bình Yên", "Trường Tiểu học Bình Yên", "Chùa Quang Phúc")).build());

                    //register vehicle for route

                    vehicleService.registerVehicleToRoute(Arrays.asList(
                            AddVehicleIntoRouteRequest.builder()
                                    .vehicleId(vehicleRepo.findAll().get(0).getId())
                                    .managerId(userRepo.findByRole(RoleEnums.TRANSPORT_MANAGER).get(0).getId())
                                    .build(),
                            AddVehicleIntoRouteRequest.builder()
                                    .vehicleId(vehicleRepo.findAll().get(1).getId())
                                    .managerId(userRepo.findByRole(RoleEnums.TRANSPORT_MANAGER).get(1).getId())
                                    .build()), route_1.getId());
                    vehicleService.registerVehicleToRoute(Arrays.asList(
                            AddVehicleIntoRouteRequest.builder()
                                    .vehicleId(vehicleRepo.findAll().get(2).getId())
                                    .managerId(userRepo.findByRole(RoleEnums.TRANSPORT_MANAGER).get(2).getId())
                                    .build(),
                            AddVehicleIntoRouteRequest.builder()
                                    .vehicleId(vehicleRepo.findAll().get(3).getId())
                                    .managerId(userRepo.findByRole(RoleEnums.TRANSPORT_MANAGER).get(3).getId())
                                    .build()), route_2.getId());
                }
            }
        }
    }

    private void AdmissionFileInitData(SchoolYearInformation savedData_academic_year) {
        admissionFileRepo.save(AdmissionFile.builder().schoolYearInformationId(savedData_academic_year).fileName("Giấy khai sinh hợp lệ").note("Bản sao có dấu đỏ").build());
        admissionFileRepo.save(AdmissionFile.builder().schoolYearInformationId(savedData_academic_year).fileName("Hộ khẩu thường trú (photo) hoặc sổ tạm trú (trên 6 tháng)").note("").build());
        admissionFileRepo.save(AdmissionFile.builder().schoolYearInformationId(savedData_academic_year).fileName("Sổ tiêm ngừa trẻ em (Photo)").note("").build());
        admissionFileRepo.save(AdmissionFile.builder().schoolYearInformationId(savedData_academic_year).fileName("Đơn xin nhập học").note("Liên hệ tại trường").build());
    }
}
