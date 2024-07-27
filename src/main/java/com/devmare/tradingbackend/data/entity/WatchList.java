package com.devmare.tradingbackend.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Setter
@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "watch_lists")
public class WatchList extends BaseEntity {

    // 6:48:43
}
