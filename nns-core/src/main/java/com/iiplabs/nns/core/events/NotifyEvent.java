package com.iiplabs.nns.core.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotifyEvent {
    
    private String webId;
    
    private String sourcePhone;

    private String destinationPhone;

}
